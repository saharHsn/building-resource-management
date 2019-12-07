package tech.builtrix.exceptions;

import java.util.Hashtable;

/**
 * Created By sahar-hoseini at 08. Jul 2019 5:53 PM
 **/
public abstract class ExceptionBase extends Exception {
    private Hashtable<String, Object> parameters = new Hashtable<>();

    public ExceptionBase(String message) {
        super(message);
    }

    public ExceptionBase() {
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append(this.getClass().getSimpleName());
        message.append(" with parameters[");
        for (String key : parameters.keySet()) {
            message.append("'");
            message.append(key);
            message.append("':'");
            message.append(this.parameters.get(key));
            message.append("',");
        }
        if (!parameters.isEmpty())
            message = new StringBuilder(message.substring(0, message.length() - 1));
        message.append("]");
        return message.toString();
    }

    public final ExceptionBase addParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    public Object getParameter(String key) {
        return this.parameters.get(key);
    }

//    public int getCode() {
//        return code;
//    }
//
//    public String getDeveloperMessage() {
//        return developerMessage;
//    }
//
//    public String getLogMessage() {
//        return logMessage;
//    }
//
//    public String getUserMessage() {
//        return userMessage;
//    }
}

