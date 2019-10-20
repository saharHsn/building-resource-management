package tech.builtrix.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created By sahar-hoseini at 08. Jul 2019 9:02 AM
 **/
@Configuration()
@EnableDynamoDBRepositories(basePackages = "tech.builtrix"
        //  , dynamoDBMapperConfigRef = "dynamoDBMapperConfig"
)
public class DynamoDBConfig {

    @Value("${amazon.access.key}")
    private String awsAccessKey;
    @Value("${amazon.access.secret-key}")
    private String awsSecretKey;
    @Value("${amazon.region}")
    private String awsRegion;
    @Value("${amazon.end-point.url}")
    private String awsDynamoDBEndPoint;

    AmazonDynamoDB dynamoDB;
    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        // Create empty DynamoDBMapperConfig builder
        DynamoDBMapperConfig.Builder builder = new DynamoDBMapperConfig.Builder();
        // Inject the table name overrider bean
        //builder.setTableNameOverride(tableNameOverrider);

        // Sadly this is a @deprecated method but new DynamoDBMapperConfig.Builder() is incomplete compared to DynamoDBMapperConfig.DEFAULT
        return new DynamoDBMapperConfig(DynamoDBMapperConfig.DEFAULT, builder.build());
    }

    /*  @Bean
      public DynamoDBMapper mapper() {
          dynamoDB = amazonDynamoDBConfig();
          return new DynamoDBMapper(dynamoDB);
      }
  */
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        //return new AmazonDynamoDBClient();
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }

   /* private AmazonDynamoDB amazonDynamoDBConfig() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }
*/

    public void createTable(CreateTableRequest tableRequest) {
        dynamoDB.createTable(tableRequest);
    }

    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public void setDynamoDB(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }
}
