package com.igor.EcoPathAPI.handler;

import com.igor.EcoPathAPI.exception.OpenMeteoIntegrationException;
import com.igor.EcoPathAPI.exception.OpenRouteIntegrationException;
import com.igor.EcoPathAPI.handler.message.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OpenRouteIntegrationException.class)
    public ResponseEntity<ErrorMessage> handleOpenRouteIntegration(OpenRouteIntegrationException ex,HttpServletRequest request){
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                502,
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorMessage);
    }

    @ExceptionHandler(OpenMeteoIntegrationException.class)
    public ResponseEntity<ErrorMessage> handleOpenMeteoIntegration(OpenMeteoIntegrationException ex,HttpServletRequest request){
        ErrorMessage errorMessage = new ErrorMessage(
                Instant.now(),
                502,
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorMessage);
    }

}
