package export.exception;

public class BaseExportException extends Exception {

    public BaseExportException(String message) {
        super(message);
    }

    public BaseExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
