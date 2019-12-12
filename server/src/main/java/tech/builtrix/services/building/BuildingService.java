package tech.builtrix.services.building;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dtos.bill.BillDto;
import tech.builtrix.dtos.bill.BuildingDto;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.building.BillType;
import tech.builtrix.models.building.Building;
import tech.builtrix.repositories.FileUploader;
import tech.builtrix.repositories.building.BuildingRepository;
import tech.builtrix.services.bill.BillParser;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.user.UserService;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class BuildingService extends GenericCrudServiceBase<Building, BuildingRepository> {

    private final FileUploader fileUploader;
    private final UserService userService;
    private final BillParser billParser;
    private final BillService billService;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository,
                           UserService userService,
                           FileUploader fileUploader,
                           BillParser parser, BillService billService) {
        super(buildingRepository);
        this.userService = userService;
        this.fileUploader = fileUploader;
        this.billParser = parser;
        this.billService = billService;
    }

    public BuildingDto findById(String id) throws NotFoundException {
        Optional<Building> optionalBuilding = this.repository.findById(id);
        if (optionalBuilding.isPresent()) {
            return new BuildingDto(optionalBuilding.get());
        } else {
            throw new NotFoundException("building", "id", id);
        }
    }

    public String save(BuildingDto buildingDto) throws ParseException, BillParseException {
        String userId = this.userService.save(buildingDto.getOwner()).getId();
        buildingDto.getOwner().setId(userId);
        Building building = new Building(buildingDto);
        building = this.repository.save(building);

        //uploadFile(building.getId(), buildingDto.getGasBill(), BillType.Gas);
        String buildingId = building.getId();
        String bucketName = "metrics-building-" + buildingId;
        List<String> fileNames = uploadFile(bucketName, buildingDto.getElectricityBill(), BillType.Electricity);
        for (String fileName : fileNames) {
            if (fileName.endsWith(".pdf")) {
                logger.info("Trying to parse file : " + fileName + " for building: " + buildingId);
                BillDto billDto = this.billParser.parseBill(buildingId, bucketName, fileName);
                this.billService.save(billDto);
                logger.info("Parse done!");
            }
        }
        //uploadFile(building.getId(), buildingDto.getWaterBill(), BillType.Water);
        return buildingId;
    }

    private List<String> uploadFile(String bucketName, MultipartFile file, BillType billType) {
        if (file != null) {
            Map<String, String> metaData = new HashMap<>();
            metaData.put("BillType", billType.name());
            //bucket name should not contain uppercase characters
            return this.fileUploader.uploadFile(file, metaData, bucketName);
        }
        return null;
    }

    public void update(BuildingDto building) throws ParseException, BillParseException {
        save(building);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }

    public Iterable<Building> findAll() {
        return this.repository.findAll();
    }

    public void delete(String buildingId) {
        this.repository.deleteById(buildingId);
    }

    public void saveFile(MultipartFile file, String buildingId, BillType billType) {
        System.out.println();
    }
}
