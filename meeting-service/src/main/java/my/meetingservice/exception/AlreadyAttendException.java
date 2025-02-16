package my.meetingservice.exception;

public class AlreadyAttendException extends RuntimeException {
    public AlreadyAttendException() {
    }

    public AlreadyAttendException(String message) {
        super(message);
    }

    public AlreadyAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyAttendException(Throwable cause) {
        super(cause);
    }

    public AlreadyAttendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
