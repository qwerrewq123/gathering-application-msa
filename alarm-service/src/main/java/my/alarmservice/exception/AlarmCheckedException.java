package my.alarmservice.exception;

public class AlarmCheckedException extends RuntimeException{
    public AlarmCheckedException() {
    }

    public AlarmCheckedException(String message) {
        super(message);
    }

    public AlarmCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlarmCheckedException(Throwable cause) {
        super(cause);
    }

    public AlarmCheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
