package tech.builtrix.web.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.models.building.Building;
import tech.builtrix.models.building.enums.BuildingAge;
import tech.builtrix.models.building.enums.BuildingUsage;
import tech.builtrix.models.building.enums.EnergyCertificate;
import tech.builtrix.web.dtos.EntityDtoBase;
import tech.builtrix.web.dtos.user.UserDto;

import java.util.Date;
import java.util.Map;

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
	private Float area;
	private Integer numberOfPeople;
	private MultipartFile gasBill;
	private MultipartFile waterBill;
	private MultipartFile electricityBill;
	private UserDto owner;
	private Map<Date, Integer> numOfPeopleMap;

	public BuildingDto(Building building) {
		this.name = building.getName();
		this.usage = building.getUsage();
		this.id = building.getId();
		this.postalAddress = building.getPostalAddress();
		this.postalCode = building.getPostalCode();
		this.energyCertificate = building.getEnergyCertificate();
		this.age = building.getAge();
		this.area = building.getArea();
		this.numberOfPeople = building.getNumberOfPeople();
		// this.owner = new UserDto(building.getOwner());
		// this.owner = building.();
		// this.numOfPeopleMap = building.getNumOfPeopleMap();

	}
}
