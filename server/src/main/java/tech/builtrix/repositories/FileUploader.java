package tech.builtrix.repositories;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileUploader {

	// returns list of file names
	List<String> uploadFile(MultipartFile file, String bucketName, String pathName, Map<String, String> metaData);
}
