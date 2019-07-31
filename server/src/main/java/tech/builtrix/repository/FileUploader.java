package tech.builtrix.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploader {

    String uploadFile(MultipartFile file, Map<String, String> metaData, String bucketName);
}
