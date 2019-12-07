package tech.builtrix.exceptions.session;

import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

@ErrorMessage(code = 111)
public class DuplicatedHeaderException extends ExceptionBase {
    public DuplicatedHeaderException(String headerName) {
        addParameter("header", headerName);
    }

}
