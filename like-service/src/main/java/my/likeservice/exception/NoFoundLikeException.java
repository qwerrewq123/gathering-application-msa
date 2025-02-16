package my.likeservice.exception;

public class NoFoundLikeException extends RuntimeException{
    public NoFoundLikeException() {
    }

    public NoFoundLikeException(String message) {
        super(message);
    }

    public NoFoundLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFoundLikeException(Throwable cause) {
        super(cause);
    }

    public NoFoundLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
