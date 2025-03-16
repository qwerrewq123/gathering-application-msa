package exception.image;

public class ImageUploadFailException extends RuntimeException{
    public ImageUploadFailException() {
    }

    public ImageUploadFailException(String message) {
        super(message);
    }

    public ImageUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageUploadFailException(Throwable cause) {
        super(cause);
    }

    public ImageUploadFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
