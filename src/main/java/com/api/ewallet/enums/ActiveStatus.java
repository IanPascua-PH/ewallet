package com.api.ewallet.enums;

public enum ActiveStatus {

    ACTIVE("1", "Active"),
    INACTIVE("0", "Inactive");

    private final String code;
    private final String description;

    ActiveStatus(String code, String description) {
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
        for (ActiveStatus status : ActiveStatus.values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        return null;
    }

}
