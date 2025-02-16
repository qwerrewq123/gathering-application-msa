package my.userservice.exception;

public class FailSaveImageException extends RuntimeException {
    public FailSaveImageException() {
    }

    public FailSaveImageException(String message) {
        super(message);
    }

    public FailSaveImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailSaveImageException(Throwable cause) {
        super(cause);
    }

    public FailSaveImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
