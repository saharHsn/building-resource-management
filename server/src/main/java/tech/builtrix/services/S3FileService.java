package tech.builtrix.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.repositories.FileUploader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class S3FileService implements FileUploader {
    @Value("${amazon.access.key}")
    private String awsAccessKey;
    @Value("${amazon.access.secret-key}")
    private String awsSecretKey;
    @Value("${amazon.region}")
    private String awsRegion;

    @Override
    public List<String> uploadFile(MultipartFile file, String bucketName, String pathName,
                                   Map<String, String> metaData) {
        logger.info("Uploading file : " + file.getOriginalFilename() + " with bucket name and pathName : " + bucketName
                + " ," + pathName);
        List<String> fileNames = new ArrayList<>();
        boolean isFileExist = uploadSingleFile(file, bucketName, pathName, metaData);
        if (!isFileExist) {
            logger.info("File doesnt exist in this bucket we create it ...");
            if (!StringUtils.isEmpty(file.getOriginalFilename())) {
                fileNames.add(file.getOriginalFilename());
            } else {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    private boolean uploadSingleFile(MultipartFile file, String bucketName, String pathName,
                                     Map<String, String> metaData) {
        ObjectMetadata omd = getObjectMetadata(file, metaData);
        AmazonS3 s3 = getAmazonS3();
        if (exists(s3, bucketName, pathName)) {
            return true;
        }
        if (s3.doesBucketExistV2(bucketName)) {
            logger.info("Bucket name is available.");
        } else {
            s3.createBucket(bucketName);
        }
        String result = "Upload unsuccessfull because ";
        try {
            S3Object s3Object = new S3Object();
            ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());
            s3Object.setObjectContent(bis);
            // "bill-" + UUID.randomUUID().toString()

            s3.putObject(new PutObjectRequest(bucketName, pathName, bis, null));
            s3Object.close();
            result = "Uploaded Successfully.";
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                    + "rejected with an error response for some reason.");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
            result = result + ase.getMessage();
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException, which means the client encountered an internal error while "
                    + "trying to communicate with S3, such as not being able to access the network.");

            result = result + ace.getMessage();
        } catch (Exception e) {
            result = result + e.getMessage();
        }
        logger.info(result);
        return false;
    }

    public boolean exists(AmazonS3 s3, String bucket, String fileName) {
        logger.info("Checking if file exists in this bucket ...");
        try {
            s3.getObjectMetadata(bucket, fileName);
        } catch (AmazonServiceException e) {
            return false;
        }
        return true;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file, Map<String, String> metaData) {
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(file.getContentType());
        omd.setContentLength(file.getSize());
        omd.setHeader("filename", file.getName());
        omd.setUserMetadata(metaData);
        return omd;
    }

    public S3ObjectInputStream downloadFile(String bucketName, String fileName) throws IOException {

        S3Object fullObject = null, objectPortion = null;
        try {
            AmazonS3 s3Client = getAmazonS3();
            // Get an object and print its contents.
            System.out.println("Downloading an object");
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");
            // displayTextInputStream(fullObject.getObjectContent());

            // Get a range of bytes from an object and print the bytes.
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, fileName).withRange(0, 9);
            objectPortion = s3Client.getObject(rangeObjectRequest);
            System.out.println("Printing bytes retrieved.");
            // displayTextInputStream(objectPortion.getObjectContent());

            // Get an entire object, overriding the specified response headers, and print
            // the object's content.
            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                    .withCacheControl("No-cache")
                    .withContentDisposition("attachment; filename=example.txt");
            GetObjectRequest
                    getObjectRequestHeaderOverride = new GetObjectRequest(bucketName, fileName)
                    .withResponseHeaders(headerOverrides);
            S3Object headerOverrideObject = s3Client.getObject(getObjectRequestHeaderOverride);
            return headerOverrideObject.getObjectContent();
            // displayTextInputStream(headerOverrideObject.getObjectContent());
        } catch (SdkClientException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } // Amazon S3 couldn't be contacted for a response, or the client
// couldn't parse the response from Amazon S3.
        finally {
            // To ensure that the network connection doesn't remain open, close any open
            // input streams.
            if (fullObject != null) {
                fullObject.close();
            }
            if (objectPortion != null) {
                objectPortion.close();
            }
        }
        return null;
    }

    private AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder.standard().withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }
}
