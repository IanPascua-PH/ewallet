package com.api.ewallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                "Wallet", "Wallet not found",
                "Recipient", "Recipient not found",
                "Transaction", "Transaction not found"
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

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransaction(InvalidTransactionException ex) {
        log.error("Invalid transaction: {}", ex.getMessage());
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("EWLBE608", ex.getMessage()));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        log.error("Insufficient balance: {}", ex.getMessage());
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("EWLBE609", ex.getMessage()));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .errorDetails(new ArrayList<>())
                .build();

        ex.getBindingResult().getAllErrors().forEach((errorItem) -> {
            if (errorItem instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = errorItem.getDefaultMessage();
                error.add("EWLVE999",  errorMessage);
            } else {
                String objectName = errorItem.getObjectName();
                String errorMessage = errorItem.getDefaultMessage();
                error.add("EWLVE999", errorMessage);
            }
        });

        log.error("Validation error: {}", error.getErrorDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TransactionFailedException.class)
    public ResponseEntity<ErrorResponse> handleTransactionFailed(TransactionFailedException ex) {
        log.error("Transaction failed: {}", ex.getMessage());
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("EWLSE610", ex.getMessage()));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
    }

    @ExceptionHandler(DailyLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleDailyLimitExceeded(DailyLimitExceededException ex) {
        log.error("Daily limit exceeded: {}", ex.getMessage());
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("EWLBE619", ex.getMessage()));
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .errorDetails(details)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
