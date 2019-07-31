package tech.builtrix.common.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LocalizationService {

    private final MessageSource messageSource;

    @Autowired
    public LocalizationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Cacheable(cacheNames = "LocalizationService.localizedText")
    public String localizedText(String path, Object... params) {
        try {
            return this.messageSource.getMessage(path, params, null);
        } catch (NoSuchMessageException ex) {
            logger.warn(String.format("Message [%s] not found", path));
            return path;
        }
    }

    @Cacheable(cacheNames = "LocalizationService.localizedError")
    public String localizedError(String path) {
        return this.localizedText("errors." + path);
    }

    @Cacheable(cacheNames = "LocalizationService.localizedText")
    public String localizedText(String path) {
        try {
            return this.messageSource.getMessage(path, null, null);
        } catch (NoSuchMessageException ex) {
            logger.warn(String.format("Message [%s] not found", path));
            return path;
        }
    }

}

