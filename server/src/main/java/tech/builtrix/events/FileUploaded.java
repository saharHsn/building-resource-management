package tech.builtrix.events;

import lombok.*;

import java.util.List;

/**
 * Created By sahar at 12/16/19
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileUploaded {
    private String bucketName;
    private List<String> fileNames;
    private String buildingId;
}
