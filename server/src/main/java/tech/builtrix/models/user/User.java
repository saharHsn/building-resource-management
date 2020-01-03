package tech.builtrix.models.user;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.Setter;
import org.jboss.aerogear.security.otp.api.Base32;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.EnumConverter;
import tech.builtrix.web.dtos.user.UserDto;

import java.util.Date;


@DynamoDBTable(tableName = "User")
@Setter
@Getter
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
    private String parent;
    /*@DynamoDBAttribute
    private List<UserToken> tokens;*/
    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "Role")
    private Role role;
    @DynamoDBAttribute
    private Boolean enabled;
    @DynamoDBAttribute
    private Boolean isUsing2FA;
    @DynamoDBAttribute
    private String secret;

    //Transient property-never save it
    private String rawPassword;

    /* @Column(name = "failed_logins", nullable = false)
    private Byte failedLogin = 0;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter(value = AccessLevel.PROTECTED)
    @NotAudited
    private List<UserLogin> logins;
*/

    public User() {
        super();
        this.secret = Base32.random();
        this.enabled = false;
    }

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
