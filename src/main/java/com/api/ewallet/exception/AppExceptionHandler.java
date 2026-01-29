package com.api.ewallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        log.error("NotFoundException: {}", ex.getMessage());
        List<ErrorDetail> details = new ArrayList<>();
        Map<String, String> messageMap = Map.of(
                "User", "User not found",
                "Wallet", "Wallet not found"
        );
        String message = messageMap.getOrDefault(ex.getMessage(), ex.getMessage());
        details.add(new ErrorDetail("EWLBE404", message));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found Error")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServer(InternalServerException ex) {
        log.error("Unexpected error: ", ex);
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("EWLSE999", "Backend service error"));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
    }
}
