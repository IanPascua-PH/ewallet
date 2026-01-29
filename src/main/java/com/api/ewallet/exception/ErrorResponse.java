package com.api.ewallet.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ErrorResponse<T> {

    private int status;

    private String error;

    private List<ErrorDetail> errorDetails = new ArrayList<>();

    public ErrorResponse<T> add(String code, String message) {
        return add(new ErrorDetail(code, message));
    }

    public ErrorResponse<T> add(ErrorDetail detail) {
        this.getErrorDetails().add(detail);
        return this;
    }

}