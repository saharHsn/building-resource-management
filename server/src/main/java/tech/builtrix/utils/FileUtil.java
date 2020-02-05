package tech.builtrix.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created By sahar at 12/12/19
 */
public class FileUtil {
	public static void unzipMultipartFile(MultipartFile file, String destination) throws IOException {
		/**
		 * save file to temp
		 */
		File zip = File.createTempFile(UUID.randomUUID().toString(), "temp");
		FileOutputStream o = new FileOutputStream(zip);
		IOUtils.copy(file.getInputStream(), o);
		o.close();

		/**
		 * unizp file from temp by zip4j
		 */
		// String destination =
		// "/Users/sahar/IdeaProjects/builtrix-metrics/server/src/main/resources/bills";
		try {
			ZipFile zipFile = new ZipFile(zip);

			zipFile.extractAll(destination);
		} catch (ZipException e) {
			e.printStackTrace();
		} finally {
			/**
			 * delete temp file
			 */
			zip.delete();
		}
	}

	public static List<File> getDirectoryFiles(File file) {
		List<File> files = new ArrayList<>();
		Iterator it = FileUtils.iterateFiles(file, null, true);
		while (it.hasNext()) {
			files.add((File) it.next());
		}
		return files;
	}

	public static MultipartFile createMultiPartFile(String fileName, String fullPath) throws IOException {
		MultipartFile multipartFile = new MockMultipartFile(fileName, new FileInputStream(new File(fullPath)));
		return multipartFile;
	}

	public static void main(String[] args) throws IOException {
		createMultiPartFile("uploadTest.zip",
				"/Users/sahar/IdeaProjects/builtrix-metrics/server/src/main/resources/bills/uploadTest.zip");
		File file = new File("test2");
		// unzipMultipartFile(multipartFile, file.getAbsolutePath());
		getDirectoryFiles(file);
		// System.out.println(FileUtil.class.getClassLoader().getResource("relative
		// path"));

	}

}
