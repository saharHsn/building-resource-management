package tech.builtrix.base;

/**
 * Created By Sahar at 12/29/19 : 3:18 PM
 **/
public abstract class ServiceException extends Exception {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @return Error contents object to be handled by client
     */
    public Object getContents() {
        return null;
    }
}
