package com.api.ewallet.enums;

public enum InvalidExceptionEnum {

    USER("User"),
    WALLET("Wallet"),
    RECIPIENT("Recipient"),
    TRANSACTION("Transaction");

    private final String code;

    InvalidExceptionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
