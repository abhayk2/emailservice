package com.abhay.userservice.exception;

import com.abhay.userservice.domain.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Hidden
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExistsException(EmailExistsException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User creation failed")
                        .reason(exception.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build(), HttpStatus.CONFLICT);
    }
    @Hidden
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("An unexpected error occurred")
                        .reason(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Hidden
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Invalid request parameter provided")
                        .reason(exception.getMessage()) // This will contain your custom message
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
