package tech.builtrix.web.dtos.building;

import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.models.building.enums.BillType;

public class UploadFileDto {
	private MultipartFile file;
	private String buildingId;
	private BillType billType;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public BillType getBillType() {
		return billType;
	}

	public void setBillType(BillType billType) {
		this.billType = billType;
	}
}
