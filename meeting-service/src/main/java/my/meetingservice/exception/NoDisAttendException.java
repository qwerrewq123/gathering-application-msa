package my.meetingservice.exception;

public class NoDisAttendException extends RuntimeException {
    public NoDisAttendException() {
    }

    public NoDisAttendException(String message) {
        super(message);
    }

    public NoDisAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDisAttendException(Throwable cause) {
        super(cause);
    }

    public NoDisAttendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
