package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Centralized validation helpers.
 */
public final class Validator {
    private Validator() {}

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName + " must not be null");
        }
        return value;
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new InvalidDataException(fieldName + " must be > 0");
        }
        return value;
    }

    public static int requireRangeInclusive(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new InvalidDataException(fieldName + " must be in range [" + min + ", " + max + "]");
        }
        return value;
    }

    public static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException(fieldName + " must be >= 0");
        }
        return value;
    }

    public static void requireEquals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new InvalidDataException(message);
        }
    }
}


