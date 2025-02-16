package my.alarmservice.exception;

public class NoFoundAlarmException extends RuntimeException{
    public NoFoundAlarmException() {
    }

    public NoFoundAlarmException(String message) {
        super(message);
    }

    public NoFoundAlarmException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFoundAlarmException(Throwable cause) {
        super(cause);
    }

    public NoFoundAlarmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
