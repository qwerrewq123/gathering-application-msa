package gathering.msa.user.advice;

import dto.response.ErrorResponse;
import exception.NotValidHeaderException;
import exception.image.ImageUploadFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import util.ConstClass;

import static util.ConstClass.*;


@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(NotValidHeaderException.class)
    public ResponseEntity<ErrorResponse> handleNotValidHeaderException(NotValidHeaderException ex) {

        ErrorResponse errorResponse = ErrorResponse.of(NOT_VALID_HEADER_CODE, NOT_VALID_HEADER_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(ImageUploadFailException.class)
    public ResponseEntity<ErrorResponse> handleImageUploadFailException(ImageUploadFailException ex) {

        ErrorResponse errorResponse = ErrorResponse.of(UPLOAD_FAIL_CODE, UPLOAD_FAIL_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
