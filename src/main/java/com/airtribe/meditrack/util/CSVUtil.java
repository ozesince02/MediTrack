package com.airtribe.meditrack.util;

import java.util.StringJoiner;

/**
 * Minimal CSV helper using {@link String#split(String)}.
 *
 * <p>Note: This is intentionally simple and does not support quoted/escaped commas.</p>
 */
public final class CSVUtil {
    private CSVUtil() {}

    public static String[] split(String line) {
        String v = Validator.requireNonBlank(line, "line");
        return v.split(",", -1);
    }

    public static String join(String... fields) {
        Validator.requireNonNull(fields, "fields");
        StringJoiner sj = new StringJoiner(",");
        for (String f : fields) {
            sj.add(f == null ? "" : f);
        }
        return sj.toString();
    }
}


