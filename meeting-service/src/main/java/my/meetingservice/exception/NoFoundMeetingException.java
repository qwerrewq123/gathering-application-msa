package my.meetingservice.exception;

public class NoFoundMeetingException extends RuntimeException {
    public NoFoundMeetingException() {
    }

    public NoFoundMeetingException(String message) {
        super(message);
    }

    public NoFoundMeetingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFoundMeetingException(Throwable cause) {
        super(cause);
    }

    public NoFoundMeetingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
