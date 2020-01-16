package tech.builtrix.services.building;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.eventbus.MessageManager;
import tech.builtrix.events.FileUploaded;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.building.BillType;
import tech.builtrix.models.building.Building;
import tech.builtrix.repositories.FileUploader;
import tech.builtrix.repositories.building.BuildingRepository;
import tech.builtrix.services.bill.BillParser;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.user.UserService;
import tech.builtrix.utils.FileUtil;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BuildingDto;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;


@Service
@Slf4j
public class BuildingService extends GenericCrudServiceBase<Building, BuildingRepository> {

    private final FileUploader fileUploader;
    private final UserService userService;
    private final BillParser billParser;
    private final BillService billService;
    private final MessageManager messageManager;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository,
                           UserService userService,
                           FileUploader fileUploader,
                           BillParser parser, BillService billService,
                           MessageManager messageManager) {
        super(buildingRepository);
        this.userService = userService;
        this.fileUploader = fileUploader;
        this.billParser = parser;
        this.billService = billService;
        this.messageManager = messageManager;
    }

    public BuildingDto findById(String id) throws NotFoundException {
        Optional<Building> optionalBuilding = this.repository.findById(id);
        if (optionalBuilding.isPresent()) {
            return new BuildingDto(optionalBuilding.get());
        } else {
            throw new NotFoundException("building", "id", id);
        }
    }

    public BuildingDto save(BuildingDto buildingDto) throws ParseException, BillParseException, NotFoundException {
        String userId;
        if (!StringUtils.isEmpty(buildingDto.getOwner().getId())) {
            userId = buildingDto.getOwner().getId();
            this.userService.update(buildingDto.getOwner());
        } else {
            userId = this.userService.save(buildingDto.getOwner()).getId();
        }
        buildingDto.getOwner().setId(userId);
        Building building = new Building(buildingDto);
        building = this.repository.save(building);
        buildingDto.setId(building.getId());
        makeFileUploadEvent(buildingDto);
        //uploadBillFiles(buildingDto);
        BuildingDto result = new BuildingDto(building);
        result.setOwner(buildingDto.getOwner());
        return result;
    }

    public BuildingDto update(BuildingDto buildingDto) throws NotFoundException {
        Building building = getUpdatedBuilding(buildingDto);
        this.repository.save(building);
        makeFileUploadEvent(buildingDto);
        BuildingDto result = new BuildingDto(building);
        result.setOwner(buildingDto.getOwner());
        return result;
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

    public BuildingDto findByOwner(String userId) {
        Building building;
        building = this.repository.findByOwner(userId);
        if (building != null) {
            return new BuildingDto(building);
        }
        return null;
    }

    @EventListener
    public void processMessage(FileUploaded event) {
        try {
            parseBillFiles(event);
        } catch (Exception e) {
            logger.error("BuildingService -> processMessage ->" + e.getCause() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    //------------------------------------ private methods ---------------------------------------

    private void parseBillFiles(FileUploaded fileUploaded) throws BillParseException, ParseException {
        for (String fileName : fileUploaded.getFileNames()) {
            if (fileName.endsWith(".pdf")) {
                logger.info("Trying to parse file : " + fileName + " for building: " + fileUploaded.getBuildingId());
                BillDto billDto = this.billParser.parseBill(fileUploaded.getBuildingId(), fileUploaded.getBucketName(),
                        fileUploaded.getBuildingId() + "-" + fileName);
                if (billDto != null) {
                    this.billService.save(billDto);
                    logger.info("Parse done!");
                }
            }
        }
    }

    private void makeFileUploadEvent(BuildingDto buildingDto) {
        String buildingId = buildingDto.getId();
        String bucketName = "metrics-building";
        List<String> fileNames = uploadFile(bucketName, buildingId, buildingDto.getElectricityBill(), BillType.Electricity);
        if (!StringUtils.isEmpty(fileNames)) {
            FileUploaded fileUploaded = new FileUploaded(buildingId, fileNames, bucketName);
            if (buildingDto.getElectricityBill() != null) {
                // publish add fileUploaded event
                this.messageManager.publish(fileUploaded);
            }
        }
    }

    private List<String> uploadFile(String bucketName, String pathName, MultipartFile file, BillType billType) {
        if (file != null) {
            Map<String, String> metaData = new HashMap<>();
            metaData.put("BillType", billType.name());
            logger.info("file content type: " + file.getContentType());
            //bucket name should not contain uppercase characters
            if (file.getContentType() != null && file.getContentType().equalsIgnoreCase("application/zip")) {
                logger.info("file:"
                        + file.getOriginalFilename() + " is zipped!");
                File destFiles = new File(pathName);
                String destination = destFiles.getAbsolutePath();
                try {
                    FileUtil.unzipMultipartFile(file, destination);
                } catch (IOException e) {
                    throw new RuntimeException("Encounter IO error in unzipping files : " + file.getName());
                }
                return uploadMultiFiles(destFiles, bucketName, pathName, metaData);
                //fileNames.addAll(uploadMultiFiles(destFiles, bucketName, pathName, metaData));
            } else {
                return this.fileUploader.uploadFile(file, bucketName, pathName + "-" + file.getOriginalFilename(), metaData);
            }
        }
        return null;
    }

    private List<String> uploadMultiFiles(File destination, String bucketName, String pathName, Map<String, String> metaData) {
        List<String> fileNames = new ArrayList<>();
        List<File> directoryFiles = FileUtil.getDirectoryFiles(destination);
        for (File directoryFile : directoryFiles) {
            MultipartFile multiPartFile;
            try {
                multiPartFile = FileUtil.createMultiPartFile(directoryFile.getName(), directoryFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Encounter error : " + e + " during creating multiPart files");
            }
            fileNames.addAll(this.fileUploader.uploadFile(multiPartFile, bucketName, pathName + "-" + multiPartFile.getName(), metaData));
        }
        return fileNames;
    }

    private Building getUpdatedBuilding(BuildingDto buildingDto) throws NotFoundException {
        String userId = this.userService.update(buildingDto.getOwner()).getId();
        Building building = getById(buildingDto.getId());
        building.setAge(buildingDto.getAge());
        building.setArea(buildingDto.getArea());
        building.setEnergyCertificate(buildingDto.getEnergyCertificate());
        building.setNumberOfPeople(buildingDto.getNumberOfPeople());
        building.setName(building.getName());
        building.setOwner(userId);
        building.setPostalAddress(buildingDto.getPostalAddress());
        building.setPostalCode(buildingDto.getPostalCode());
        building.setUsage(buildingDto.getUsage());
        building = this.repository.save(building);
        return building;
    }
}
