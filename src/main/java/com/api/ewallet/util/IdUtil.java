package com.api.ewallet.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdUtil {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String TRANSACTION = "TXN";
    private static final String REFERENCE = "REF";

    /**
     * Example: TXN20260129175345AB12
     */
    public static String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String randomSuffix = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 4)
                .toUpperCase();
        return TRANSACTION + timestamp + randomSuffix;
    }

    /**
     * Example: REF5F2A9C7D8E12
     */
    public static String generateReferenceId() {
        String shortUuid = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
        return REFERENCE + shortUuid;
    }
}

