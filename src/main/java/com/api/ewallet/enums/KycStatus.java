package com.api.ewallet.enums;

public enum KycStatus {

    VERIFIED("1"),
    UNVERIFIED("0");

    private final String code;

    KycStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
