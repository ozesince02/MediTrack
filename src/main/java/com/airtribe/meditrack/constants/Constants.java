package com.airtribe.meditrack.constants;

import java.math.BigDecimal;

/**
 * Application-wide constants and configuration defaults.
 */
public final class Constants {
    private Constants() {}

    public static final BigDecimal DEFAULT_TAX_RATE;
    public static final String DEFAULT_CURRENCY;

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    static {
        // Demonstrates static initialization.
        String tax = System.getProperty("meditrack.taxRate", "0.18");
        DEFAULT_TAX_RATE = new BigDecimal(tax);
        DEFAULT_CURRENCY = System.getProperty("meditrack.currency", "INR");
    }
}


