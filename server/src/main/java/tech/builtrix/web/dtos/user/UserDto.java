package tech.builtrix.web.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.user.Gender;
import tech.builtrix.models.user.Role;
import tech.builtrix.models.user.User;
import tech.builtrix.web.dtos.EntityDtoBase;

import java.util.Date;

/**
 * Created By sahar-hoseini at 11. Jul 2019 5:53 PM
 **/

@JsonInclude
@Data
@NoArgsConstructor
public class UserDto extends EntityDtoBase {
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phone;
	private String job;
	@ApiModelProperty(notes = "Gender", dataType = "string", allowableValues = "Male,Female")
	private Gender gender;
	@ApiModelProperty(notes = "Birth date in unix timestamp", dataType = "integer")
	private Date birthDate;
	private Boolean isActive;
	private Role role;
	private String password;
	private String token;

	public UserDto(User user) {
		this.id = user.getId();
		this.birthDate = user.getBirthDate();
		this.emailAddress = user.getEmailAddress();
		this.job = user.getJob();
		this.firstName = user.getFirstName();
		this.gender = user.getGender();
		this.lastName = user.getLastName();
		this.phone = user.getPhoneNumber();
		this.isActive = user.getActive();
		this.role = user.getRole();
		// this.password = user.getPassword();
	}
}
