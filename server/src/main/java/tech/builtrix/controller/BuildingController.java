package tech.builtrix.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.Response;
import tech.builtrix.dto.BuildingDto;
import tech.builtrix.exception.NotFoundException;
import tech.builtrix.model.building.BillType;
import tech.builtrix.service.building.BuildingService;
import tech.builtrix.util.FileService;

import java.io.IOException;

/**
 * Created By sahar-hoseini at 08. Jul 2019 5:53 PM
 **/
@RestController
@RequestMapping("/v1/buildings")
@Api(value = "Building Controller", tags = {"Building Controller"})
public class BuildingController {
    private final BuildingService service;
    private final FileService fileService;

    public BuildingController(BuildingService service, FileService fileService) {
        this.service = service;
        this.fileService = fileService;
    }

    @ApiOperation(value = "Request for creating new building")
    @PostMapping
    public Response<String> save(@ModelAttribute BuildingDto building) {
        String buildingId = service.save(building);
        return Response.ok(buildingId);
    }

    @ApiOperation(value = "Request for getting a building details")
    @GetMapping(value = "{buildingId}")
    public Response<BuildingDto> get(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        BuildingDto building = service.findById(buildingId);
        return Response.ok(building);
    }

    @ApiOperation(value = "Request for updating a specific building")
    @PutMapping
    public Response<Void> update(@RequestBody BuildingDto building) {
        service.update(building);
        return Response.ok();
    }

    @ApiOperation(value = "Request for deleting a specific building")
    @DeleteMapping(value = "{buildingId}")
    public Response<Void> deleteBuildingDetails(@PathVariable("buildingId") String buildingId) {
        service.delete(buildingId);
        return Response.ok();
    }

    @ApiOperation(value = "Request for uploading a file of type GAS - ELECTRICITY or WATER for specific building")
    @PostMapping(value = "/files")
    public Response<Void> uploadFile(@ModelAttribute UploadFileDto model) {
        /* */
        //fileService.storeFile(file);
        // this.service.saveFile(file, buildingId, billType);
        return Response.ok();
    }
}
