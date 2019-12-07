package tech.builtrix.exceptions;


import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

import java.util.Map;

/**
 * Created By sahar-hoseini at 05. Jul 2019 5:53 PM
 **/

@ErrorMessage(code = 404001, status = HttpStatus.NOT_FOUND)
public class NotFoundException extends ExceptionBase {
    public NotFoundException(String entityName, String field, Object value) {
        addParameter("entity", entityName)
                .addParameter("field", field)
                .addParameter("value", value);
    }

    public NotFoundException(String entityName, Map<String, String> filedAndValue) {
        addParameter("entity", entityName);
        for (Map.Entry<String, String> entry : filedAndValue.entrySet())
        {
            addParameter(entry.getKey() , entry.getValue());
        }
    }
}


