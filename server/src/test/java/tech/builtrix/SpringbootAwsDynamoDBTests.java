package tech.builtrix;

import com.amazonaws.services.dynamodbv2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.configurations.DynamoDBConfig;
import tech.builtrix.models.building.Building;
import tech.builtrix.models.building.BuildingAge;
import tech.builtrix.models.building.BuildingUsage;
import tech.builtrix.models.building.EnergyCertificate;
import tech.builtrix.models.user.Education;
import tech.builtrix.models.user.Gender;
import tech.builtrix.models.user.User;
import tech.builtrix.repositories.building.BuildingRepository;
import tech.builtrix.repositories.user.UserRepository;
import tech.builtrix.services.building.BuildingService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootAwsDynamoDBTests {
	@Autowired
	private DynamoDBConfig dynamoDBConfig;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	BuildingService buildingService;

	private static final String FIRST_NAME = "";
	private static final String BUILDING_NAME = "";

	@Before
	public void setup() throws Exception {
		boolean userTableExist = false;
		boolean addressTableExist = false;
		boolean buildingTableExist = false;

		/*
		 * CreateTableRequest createBuildingTableRequest =
		 * dynamoDBMapper.generateCreateTableRequest(Building.class);
		 * createBuildingTableRequest.withProvisionedThroughput(new
		 * ProvisionedThroughput(2L, 2L));
		 * dynamoDBConfig.getDynamoDB().createTable(createBuildingTableRequest);
		 */
		/*
		 * Table table = dynamoDB.createTable(TABLE_NAME, Collections.singletonList(new
		 * KeySchemaElement("id", KeyType.HASH)), Collections.singletonList(new
		 * AttributeDefinition("id", ScalarAttributeType.S)), new
		 * ProvisionedThroughput(10L, 10L)); table.waitForActive();
		 */
		createTable("User");
		createTable("Building");
		createTable("Address");
		createTable("User_Token");
		createTable("Bill");
		createTable("Bill_Parameter_Info");
		createTable("Verification_Token");
		createTable("Session");
		createTable("Internal_Messages");
	}

	private void createTable(String tableName) {
		dynamoDBConfig.getDynamoDB().createTable(
				Collections.singletonList(new AttributeDefinition("id", ScalarAttributeType.S)), tableName,
				Collections.singletonList(new KeySchemaElement("id", KeyType.HASH)),
				new ProvisionedThroughput(10L, 10L));
	}

	/*
	 * private void createTable() { CreateTableRequest createUserTableRequest =
	 * dynamoDBMapper.generateCreateTableRequest(User.class);
	 * createUserTableRequest.withProvisionedThroughput(new
	 * ProvisionedThroughput(2L, 2L));
	 * //dynamoDBConfig.getDynamoDB().createTable(createUserTableRequest);
	 * dynamoDBConfig.getDynamoDB().createTable(Collections.singletonList(new
	 * AttributeDefinition("id", ScalarAttributeType.S)), "User",
	 * Collections.singletonList(new KeySchemaElement("id", KeyType.HASH)), new
	 * ProvisionedThroughput(10L, 10L)); }
	 */

	/*
	 * private void createTableIfDoesNotExist(boolean tableExist, Class entity)
	 * throws InterruptedException { if
	 * (TableUtils.createTableIfNotExists(dynamoDBConfig.getDynamoDB(),
	 * dynamoDBMapper.generateCreateTableRequest(entity)
	 * .withProvisionedThroughput(new ProvisionedThroughput(2L, 2L)))) {
	 * //logger.info("~~~ Waiting for User table to be active ~~~");
	 * TableUtils.waitUntilActive(dynamoDBConfig.getDynamoDB(),
	 * entity.getSimpleName()); } else {
	 * //logger.info("~~~  table already exists ~~~"); } }
	 * 
	 * private void makeTable(Class aClass) { CreateTableRequest
	 * createUserTableRequest = dynamoDBMapper.generateCreateTableRequest(aClass);
	 * createUserTableRequest.withProvisionedThroughput(new
	 * ProvisionedThroughput(2L, 2L));
	 * dynamoDBConfig.getDynamoDB().createTable(createUserTableRequest); }
	 */

	@After
	public void onFinish() throws Exception {
		/*
		 * dynamoDBConfig.getDynamoDB().deleteTable("User");
		 * dynamoDBConfig.getDynamoDB().deleteTable("Building");
		 */
	}

	public SpringbootAwsDynamoDBTests() {
	}

	@Test
	public void dynamoDBTestCase() {
		// buildingTest();
		List<User> users = userRepository.findByEmailAddress("test@test.com");
		System.out.println(users);
	}

	private User userTest() {
		User user = new User();
		user.setActive(true);
		user.setFirstName("Sahar Sadat");
		user.setLastName("Hosseini");
		user.setBirthDate(new Date());
		user.setEducation(Education.Bachelor);
		user.setEmailAddress("hoseinisahar999@gmail.com");
		user.setGender(Gender.Female);
		user.setJob("Project Manager");
		user.setHomeTown("Iran");
		user.setPhoneNumber("+989399325216");
		userRepository.save(user);

		List<User> users = (List<User>) userRepository.findAll();

		/*
		 * assertTrue("User found.", users.size() > 0);
		 * assertTrue("The user first name is correct.",
		 * users.get(0).getFirstName().equals(FIRST_NAME));
		 */
		return user;
	}

	private void buildingTest() {
		Building building = new Building();
		building.setAge(BuildingAge.BETWEEN_10_TO_15_YEARS);
		building.setArea(500);
		building.setEnergyCertificate(EnergyCertificate.A);
		building.setName(BUILDING_NAME);
		building.setNumberOfPeople(500);
		building.setUsage(BuildingUsage.OFFICE_BUILDING_OR_CO_WORK_SPACE);
		/*
		 * Address fullAddress = new Address(); fullAddress.
		 * setPostalAddress("floor 3, building no 16, Kaman st, Shahidi st, Vanak sq, Tehran, Iran"
		 * ); fullAddress.setPostalCode("111122223333");
		 * dynamoDBMapper.save(fullAddress);
		 * building.setFullAddress(fullAddress.getId());
		 */User user = userTest();
		building.setOwner(user.getId());
		// buildingService.save(building);
		buildingRepository.save(building);

		// List<Building> buildings = (List<Building>) buildingRepository.findAll();

		/*
		 * assertTrue("Building found.", buildings.size() > 0);
		 * assertTrue("The building name is correct.",
		 * buildings.get(0).getName().equals(BUILDING_NAME));
		 */
	}
}
