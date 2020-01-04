package tech.builtrix.events;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created By sahar at 12/16/19
 */

@Getter
@Setter
@NoArgsConstructor
@Data
public class FileUploaded {
    private String bucketName;
    private List<String> fileNames;
    private String buildingId;

    public FileUploaded(String buildingId, List<String> fileNames, String bucketName) {
        this.buildingId = buildingId;
        this.fileNames = fileNames;
        this.bucketName = bucketName;
    }
}
