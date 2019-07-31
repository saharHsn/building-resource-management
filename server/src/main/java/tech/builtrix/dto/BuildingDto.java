package tech.builtrix.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.model.building.Building;
import tech.builtrix.model.building.BuildingAge;
import tech.builtrix.model.building.BuildingUsage;
import tech.builtrix.model.building.EnergyCertificate;
import tech.builtrix.service.building.BuildingService;

/**
 * Created By sahar-hoseini at 11. Jul 2019 5:53 PM
 **/

@JsonInclude
@Data
@NoArgsConstructor
public class BuildingDto extends EntityDtoBase {
    private String name;
    private String postalAddress;
    protected String postalCode;
    private BuildingUsage usage = BuildingUsage.UNKNOWN;
    private EnergyCertificate energyCertificate = EnergyCertificate.Others;
    private BuildingAge age = BuildingAge.UNKNOWN;
    private float area;
    private Integer numberOfPeople;
    private MultipartFile gasBill;
    private MultipartFile waterBill;
    private MultipartFile electricityBill;
    private UserDto owner;

    public BuildingDto(Building building) {
        this.name = building.getName();
        this.usage = building.getUsage();
        this.energyCertificate = building.getEnergyCertificate();
        this.age = building.getAge();
        this.area = building.getArea();
        this.numberOfPeople = building.getNumberOfPeople();
       // this.owner = building.();

    }
}
