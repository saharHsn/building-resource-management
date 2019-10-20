package tech.builtrix.model.user;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.dto.UserDto;
import tech.builtrix.model.EnumConverter;

import java.util.Date;
import java.util.List;


@DynamoDBTable(tableName = "User")
@Setter
@Getter
@NoArgsConstructor
public class User extends EntityBase<User> {

    @DynamoDBAttribute
    private String firstName;
    @DynamoDBAttribute
    private String lastName;
    @DynamoDBAttribute
    private String job;
    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "Gender")
    private Gender gender;
    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "Education")
    private Education education;
    @DynamoDBAttribute
    private String emailAddress;
    @DynamoDBAttribute
    private String phoneNumber;
    @DynamoDBAttribute
    private Boolean emailConfirmed;
    @DynamoDBAttribute
    private Boolean phoneConfirmed;
    @DynamoDBAttribute
    private Date emailConfirmationTime;
    @DynamoDBAttribute
    private Date phoneConfirmationTime;
    @DynamoDBAttribute
    private String password;
    @DynamoDBAttribute
    private Date birthDate;
    @DynamoDBAttribute
    private String homeTown;
    @DynamoDBAttribute
    private String nationalId;

    @DynamoDBAttribute
    private List<UserToken> tokens;

    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "Role")
    private Role role;

    /* @Column(name = "failed_logins", nullable = false)
    private Byte failedLogin = 0;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter(value = AccessLevel.PROTECTED)
    @NotAudited
    private List<UserLogin> logins;
*/
    public User(UserDto userDto) {
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.job = userDto.getJob();
        this.gender = userDto.getGender();
        this.emailAddress = userDto.getEmailAddress();
        this.phoneNumber = userDto.getPhone();
        this.birthDate = userDto.getBirthDate();
        this.role = userDto.getRole();
        this.password = userDto.getPassword();
    }
}
