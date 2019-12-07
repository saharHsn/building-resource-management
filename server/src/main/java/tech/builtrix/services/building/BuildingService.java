package tech.builtrix.services.building;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dtos.bill.BuildingDto;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.building.BillType;
import tech.builtrix.models.building.Building;
import tech.builtrix.repositories.FileUploader;
import tech.builtrix.repositories.building.BuildingRepository;
import tech.builtrix.services.user.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class BuildingService extends GenericCrudServiceBase<Building, BuildingRepository> {

    private final FileUploader fileUploader;
    private final UserService userService;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, UserService userService, FileUploader fileUploader) {
        super(buildingRepository);
        this.userService = userService;
        this.fileUploader = fileUploader;
    }

    public BuildingDto findById(String id) throws NotFoundException {
        Optional<Building> optionalBuilding = this.repository.findById(id);
        if (optionalBuilding.isPresent()) {
            return new BuildingDto(optionalBuilding.get());
        } else {
            throw new NotFoundException("building", "id", id);
        }
    }

    public String save(BuildingDto buildingDto) {
        String userId = this.userService.save(buildingDto.getOwner()).getId();
        buildingDto.getOwner().setId(userId);
        Building building = new Building(buildingDto);
        building = this.repository.save(building);

        uploadFile(building.getId(), buildingDto.getGasBill(), BillType.Gas);
        uploadFile(building.getId(), buildingDto.getElectricityBill(), BillType.Electricity);
        uploadFile(building.getId(), buildingDto.getWaterBill(), BillType.Water);

        return building.getId();
    }

    private void uploadFile(String buildingId, MultipartFile file, BillType billType) {
        if (file != null) {
            Map<String, String> metaData = new HashMap<>();
            metaData.put("BillType", billType.name());
            //bucket name should not contain uppercase characters
            this.fileUploader.uploadFile(file, metaData, "builtrix-metrics-building-" + buildingId);
        }
    }

    public void update(BuildingDto building) {
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
