package com.igor.EcoPathAPI.handler;

import com.igor.EcoPathAPI.exception.base.BadRequestException;
import com.igor.EcoPathAPI.exception.base.ConflictException;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import com.igor.EcoPathAPI.exception.base.NotFoundException;
import com.igor.EcoPathAPI.handler.message.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ErrorMessage> handleIntegration(IntegrationException ex,HttpServletRequest request){
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                502,
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorMessage);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessage> handleConflict(ConflictException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

}
