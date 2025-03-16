package gathering.msa.image.advice;


import dto.response.ErrorResponse;
import exception.image.NotFoundImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static util.ConstClass.*;


@RestControllerAdvice
public class ImageControllerAdvice {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {

        ErrorResponse errorResponse = ErrorResponse.of(UPLOAD_FAIL_CODE,UPLOAD_FAIL_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(NotFoundImageException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundImageException(NotFoundImageException ex) {

        ErrorResponse errorResponse = ErrorResponse.of(NOT_FOUND_IMAGE_CODE,NOT_FOUND_IMAGE_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
