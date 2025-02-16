package my.meetingservice.exception;

public class NoFoundAttendException extends RuntimeException {
    public NoFoundAttendException() {
    }

    public NoFoundAttendException(String message) {
        super(message);
    }

    public NoFoundAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFoundAttendException(Throwable cause) {
        super(cause);
    }

    public NoFoundAttendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
