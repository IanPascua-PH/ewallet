package com.api.ewallet.enums;

public enum TransactionStatus {

    PENDING("0", "Pending"),
    COMPLETED("1", "Completed");

    private final String code;
    private final String description;

    TransactionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescription(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        return null;
    }
}