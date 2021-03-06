package tech.builtrix.base;

import org.springframework.beans.factory.annotation.Autowired;
import tech.builtrix.commons.LocalizationService;

public abstract class ServiceBase {
	@Autowired
	protected LocalizationService localizationService;

	protected String localizedText(String path, Object... params) {
		return this.localizationService.localizedText(path, params);
	}

	protected String localizedText(String path) {
		return this.localizationService.localizedText(path);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
