package tech.builtrix.parseEngine;

import com.amazonaws.auth.policy.*;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.builtrix.utils.TExtractDto;
import tech.builtrix.utils.TextractUtil;

import java.util.*;

/**
 * Created By sahar at 11/28/19
 */

@Component
@Slf4j
public class PdfParser {
	private String sqsQueueName = null;
	private String snsTopicName = null;
	private String snsTopicArn = null;
	private String sqsQueueUrl = null;
	private String sqsQueueArn = null;
	private String startJobId = null;
	private String bucket = null;
	private String document = null;
	private AmazonSQS sqs = null;
	private AmazonSNS sns = null;
	private AmazonTextract textract = null;

	public enum ProcessType {
		DETECTION, ANALYSIS
	}

	@Value("${amazon.region}")
	private String awsRegion;

	@Value("${amazon.textract.roleArn}")
	private String roleArn;

	public TExtractDto parseFile(String bucket, String document) throws Exception {
		sns = AmazonSNSClientBuilder.standard().withRegion(awsRegion).build();
		sqs = AmazonSQSClientBuilder.standard().withRegion(awsRegion).build();
		textract = AmazonTextractClientBuilder.standard().withRegion(awsRegion).build();

		createTopicAndQueue();
		List<Block> blockList = processDocument(bucket, document, roleArn, ProcessType.ANALYSIS);

		TExtractDto tExtractDto = TextractUtil.extractData(blockList);

		deleteTopicAndQueue();

		logger.info("Done!");
		return tExtractDto;
	}

	// Creates an SNS topic and SQS queue. The queue is subscribed to the topic.
	void createTopicAndQueue() {
		// create a new SNS topic
		snsTopicName = String.format("AmazonTextractTopic%s", Long.toString(System.currentTimeMillis()));
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsTopicName);
		CreateTopicResult createTopicResult = sns.createTopic(createTopicRequest);
		snsTopicArn = createTopicResult.getTopicArn();

		// Create a new SQS Queue
		sqsQueueName = "AmazonTextractQueue" + System.currentTimeMillis();
		final CreateQueueRequest createQueueRequest = new CreateQueueRequest(sqsQueueName);
		sqsQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		sqsQueueArn = sqs.getQueueAttributes(sqsQueueUrl, Arrays.asList("QueueArn")).getAttributes().get("QueueArn");

		// Subscribe SQS queue to SNS topic
		String sqsSubscriptionArn = sns.subscribe(snsTopicArn, "sqs", sqsQueueArn).getSubscriptionArn();

		// Authorize queue
		Policy policy = new Policy().withStatements(new Statement(Statement.Effect.Allow)
				.withPrincipals(Principal.AllUsers).withActions(SQSActions.SendMessage)
				.withResources(new Resource(sqsQueueArn)).withConditions(new Condition().withType("ArnEquals")
						.withConditionKey("aws:SourceArn").withValues(snsTopicArn)));

		Map queueAttributes = new HashMap();
		queueAttributes.put(QueueAttributeName.Policy.toString(), policy.toJson());
		sqs.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl, queueAttributes));

		logger.info("Topic arn: " + snsTopicArn);
		logger.info("Queue arn: " + sqsQueueArn);
		logger.info("Queue url: " + sqsQueueUrl);
		logger.info("Queue sub arn: " + sqsSubscriptionArn);
	}

	// Starts the processing of the input document.
	List<Block> processDocument(String inBucket, String inDocument, String inRoleArn, ProcessType type)
			throws Exception {
		bucket = inBucket;
		document = inDocument;
		roleArn = inRoleArn;

		switch (type) {
		case DETECTION:
			startDocumentTextDetection(bucket, document);
			logger.info("Processing type: Detection");
			break;
		case ANALYSIS:
			startDocumentAnalysis(bucket, document);
			logger.info("Processing type: Analysis");
			break;
		default:
			logger.info("Invalid processing type. Choose Detection or Analysis");
			throw new Exception("Invalid processing type");

		}

		logger.info("Waiting for job: " + startJobId);
		// Poll queue for messages
		List<Message> messages = null;
		int dotLine = 0;
		boolean jobFound = false;
		List<Block> blockList = null;
		// loop until the job status is published. Ignore other messages in queue.
		do {
			messages = sqs.receiveMessage(sqsQueueUrl).getMessages();
			if (dotLine++ >= 40) {
				dotLine = 0;
			} // logger.info("");

			if (!messages.isEmpty()) {
				// Loop through messages received.
				for (Message message : messages) {
					String notification = message.getBody();

					// Get status and job id from notification.
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jsonMessageTree = mapper.readTree(notification);
					JsonNode messageBodyText = jsonMessageTree.get("Message");
					ObjectMapper operationResultMapper = new ObjectMapper();
					JsonNode jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
					JsonNode operationJobId = jsonResultTree.get("JobId");
					JsonNode operationStatus = jsonResultTree.get("Status");
					logger.info("Job found was " + operationJobId);
					// Found job. Get the results and display.
					if (operationJobId.asText().equals(startJobId)) {
						jobFound = true;
						logger.info("Job id: " + operationJobId);
						logger.info("Status : " + operationStatus.toString());
						if (operationStatus.asText().equals("SUCCEEDED")) {
							switch (type) {
							case DETECTION:
								getDocumentTextDetectionResults();
								break;
							case ANALYSIS:
								blockList = getDocumentAnalysisResults();
								break;
							default:
								logger.info("Invalid processing type. Choose Detection or Analysis");
								throw new Exception("Invalid processing type");

							}
						} else {
							logger.error("Analysis failed");
						}

						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					} else {
						logger.info("Job received was not job " + startJobId);
						// Delete unknown message. Consider moving message to dead letter queue
						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					}
				}
			} else {
				Thread.sleep(5000);
			}
		} while (!jobFound);

		logger.info("Finished processing document");
		return blockList;
	}

	// Gets the results of processing started by StartDocumentTextDetection
	private void getDocumentTextDetectionResults() throws Exception {
		int maxResults = 1000;
		String paginationToken = null;
		GetDocumentTextDetectionResult response = null;
		Boolean finished = false;

		while (finished == false) {
			GetDocumentTextDetectionRequest documentTextDetectionRequest = new GetDocumentTextDetectionRequest()
					.withJobId(startJobId).withMaxResults(maxResults).withNextToken(paginationToken);
			response = textract.getDocumentTextDetection(documentTextDetectionRequest);
			DocumentMetadata documentMetaData = response.getDocumentMetadata();

			logger.info("Pages: " + documentMetaData.getPages().toString());

			// Show blocks information
			List<Block> blocks = response.getBlocks();

			paginationToken = response.getNextToken();
			if (paginationToken == null)
				finished = true;

		}

	}

	// Gets the results of processing started by StartDocumentAnalysis
	private List<Block> getDocumentAnalysisResults() throws Exception {
		int maxResults = 1000;
		String paginationToken = null;
		GetDocumentAnalysisResult response = null;
		Boolean finished = false;
		String allBlocks = "";
		List<Block> blockList = new ArrayList<>();
		// loops until pagination token is null
		String display = "";
		while (finished.equals(false)) {
			GetDocumentAnalysisRequest documentAnalysisRequest = new GetDocumentAnalysisRequest().withJobId(startJobId)
					.withMaxResults(maxResults).withNextToken(paginationToken);

			response = textract.getDocumentAnalysis(documentAnalysisRequest);

			DocumentMetadata documentMetaData = response.getDocumentMetadata();

			logger.info("Pages: " + documentMetaData.getPages().toString());

			// Show blocks, confidence and detection times
			List<Block> blocks = response.getBlocks();
			blockList.addAll(blocks);
			paginationToken = response.getNextToken();
			if (paginationToken == null) {
				finished = true;
			}
		}

		return blockList;
	}

	private void startDocumentTextDetection(String bucket, String document) throws Exception {
		// Create notification channel
		NotificationChannel channel = new NotificationChannel().withSNSTopicArn(snsTopicArn).withRoleArn(roleArn);

		StartDocumentTextDetectionRequest req = new StartDocumentTextDetectionRequest()
				.withDocumentLocation(
						new DocumentLocation().withS3Object(new S3Object().withBucket(bucket).withName(document)))
				.withJobTag("DetectingText").withNotificationChannel(channel);

		StartDocumentTextDetectionResult startDocumentTextDetectionResult = textract.startDocumentTextDetection(req);
		startJobId = startDocumentTextDetectionResult.getJobId();
	}

	private void startDocumentAnalysis(String bucket, String document) throws Exception {
		// Create notification channel
		NotificationChannel channel = new NotificationChannel().withSNSTopicArn(snsTopicArn).withRoleArn(roleArn);

		StartDocumentAnalysisRequest req = new StartDocumentAnalysisRequest().withFeatureTypes("TABLES", "FORMS")
				.withDocumentLocation(
						new DocumentLocation().withS3Object(new S3Object().withBucket(bucket).withName(document)))
				.withJobTag("AnalyzingText").withNotificationChannel(channel);

		StartDocumentAnalysisResult startDocumentAnalysisResult = textract.startDocumentAnalysis(req);
		startJobId = startDocumentAnalysisResult.getJobId();
	}

	void deleteTopicAndQueue() {
		if (sqs != null) {
			sqs.deleteQueue(sqsQueueUrl);
			logger.info("SQS queue deleted");
		}

		if (sns != null) {
			sns.deleteTopic(snsTopicArn);
			logger.info("SNS topic deleted");
		}
	}
}
