package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date parsing and formatting utilities.
 */
public final class DateUtil {
    private DateUtil() {}

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);

    public static LocalDate parseDate(String input, String fieldName) {
        String v = Validator.requireNonBlank(input, fieldName);
        try {
            return LocalDate.parse(v, DATE_FMT);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException(fieldName + " must match pattern " + Constants.DATE_PATTERN, e);
        }
    }

    public static LocalDateTime parseDateTime(String input, String fieldName) {
        String v = Validator.requireNonBlank(input, fieldName);
        try {
            return LocalDateTime.parse(v, DATE_TIME_FMT);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException(fieldName + " must match pattern " + Constants.DATE_TIME_PATTERN, e);
        }
    }

    public static String formatDate(LocalDate date) {
        return Validator.requireNonNull(date, "date").format(DATE_FMT);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return Validator.requireNonNull(dateTime, "dateTime").format(DATE_TIME_FMT);
    }
}


