package tech.builtrix.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.User;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.web.dtos.bill.BuildingDto;
import tech.builtrix.web.dtos.building.UploadFileDto;

import java.text.ParseException;
import java.util.List;

/**
 * Created By sahar-hoseini at 08. Jul 2019 5:53 PM
 **/
@RestController
@RequestMapping("/v1/buildings")
@Api(value = "Building Controller", tags = {"Building Controller"})
public class BuildingController extends ControllerBase {
	private final BuildingService buildingService;

	public BuildingController(BuildingService service) {
		this.buildingService = service;
	}

	@ApiOperation(value = "Request for creating new building")
	@PostMapping
	public Response<BuildingDto> save(@ModelAttribute BuildingDto building) {
		BuildingDto buildingDto = null;
		try {
			buildingDto = buildingService.save(building);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buildingDto);
	}

	@ApiOperation(value = "Request for getting a building details")
	@GetMapping(value = "{buildingId}")
	public Response<BuildingDto> get(@PathVariable("buildingId") String buildingId) throws NotFoundException {
		BuildingDto building = buildingService.findById(buildingId);
		return Response.ok(building);
	}

	@ApiOperation(value = "Request for getting a building details")
	@GetMapping(value = "/findByOwner")
	public Response<BuildingDto> getByOwner(@RequestParam String userId) {
		User user = this.requestContext.getUser();
		BuildingDto building = buildingService.findByOwner(userId);
		return Response.ok(building);
    }

    @ApiOperation(value = "Request for getting all buildings that belongs to the user or user can see them")
    @GetMapping(value = "/getAllUserBuildings")
    public Response<List<BuildingDto>> getAllUserBuildings() {
        User user = this.requestContext.getUser();
        List<BuildingDto> userBuildings = this.buildingService.getAllUserBuildings(user);
        return Response.ok(userBuildings);
    }

    @ApiOperation(value = "Request for getting all buildings")
    @GetMapping(value = "/allBuildings")
    public Response<List<BuildingDto>> getAllBuildings() {
        List<BuildingDto> allBuildings = this.buildingService.getAll();
        return Response.ok(allBuildings);
    }

    @ApiOperation(value = "Request for updating a specific building")
    @PutMapping
    public Response<BuildingDto> update(@ModelAttribute BuildingDto building)
			throws ParseException, BillParseException, NotFoundException {
		return Response.ok(this.buildingService.update(building));
	}

	@ApiOperation(value = "Request for deleting a specific building")
	@DeleteMapping(value = "{buildingId}")
	public Response<Void> deleteBuildingDetails(@PathVariable("buildingId") String buildingId) {
		buildingService.delete(buildingId);
		return Response.ok();
	}

	@ApiOperation(value = "Request for deleting a specific building")
	@DeleteMapping(value = "/deleteAllBills/{buildingId}")
	public Response<Void> deleteAllBills(@PathVariable("buildingId") String buildingId) throws NotFoundException {
		buildingService.deleteAllBills(buildingId);
		return Response.ok();
	}

	@ApiOperation(value = "Request for uploading a file of type GAS - ELECTRICITY or WATER for specific building")
	@PostMapping(value = "/files")
	public Response<Void> uploadFile(@ModelAttribute UploadFileDto model) {
		/* */
		// fileService.storeFile(file);
		// this.service.saveFile(file, buildingId, billType);
		return Response.ok();
	}
}
