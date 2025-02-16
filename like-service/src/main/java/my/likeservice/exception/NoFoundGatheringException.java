package my.likeservice.exception;

public class NoFoundGatheringException extends RuntimeException{
    public NoFoundGatheringException() {
    }

    public NoFoundGatheringException(String message) {
        super(message);
    }

    public NoFoundGatheringException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFoundGatheringException(Throwable cause) {
        super(cause);
    }

    public NoFoundGatheringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
