package exception;

public class NotValidHeaderException extends RuntimeException {
    public NotValidHeaderException() {
    }

    public NotValidHeaderException(String message) {
        super(message);
    }

    public NotValidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidHeaderException(Throwable cause) {
        super(cause);
    }

    public NotValidHeaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
