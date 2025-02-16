package my.likeservice.exception;

public class AlreadyLikeException extends RuntimeException{
    public AlreadyLikeException() {
    }

    public AlreadyLikeException(String message) {
        super(message);
    }

    public AlreadyLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyLikeException(Throwable cause) {
        super(cause);
    }

    public AlreadyLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
