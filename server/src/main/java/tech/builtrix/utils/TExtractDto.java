package tech.builtrix.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created By sahar at 11/29/19
 */
@JsonInclude
@Data
@NoArgsConstructor
@Getter
@Setter
public class TExtractDto {
	List<MyTable> tablesResult;
	Map<String, String> keyValueResult;

	public TExtractDto(List<MyTable> tableResult, Map<String, String> keyValueResult) {
		this.tablesResult = tableResult;
		this.keyValueResult = keyValueResult;
	}
}
