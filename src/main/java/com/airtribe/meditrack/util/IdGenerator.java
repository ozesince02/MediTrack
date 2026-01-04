package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton ID generator backed by an {@link AtomicInteger}.
 *
 * <p>Demonstrates: Singleton (lazy), static initialization, AtomicInteger.</p>
 */
public final class IdGenerator {
    private static final int INITIAL_VALUE;

    static {
        String configured = System.getProperty("meditrack.id.start", "1000");
        int parsed;
        try {
            parsed = Integer.parseInt(configured);
        } catch (NumberFormatException e) {
            parsed = 1000;
        }
        INITIAL_VALUE = parsed;
    }

    private final AtomicInteger counter = new AtomicInteger(INITIAL_VALUE);

    private static volatile IdGenerator instance;

    private IdGenerator() {}

    public static IdGenerator getInstance() {
        IdGenerator local = instance;
        if (local == null) {
            synchronized (IdGenerator.class) {
                local = instance;
                if (local == null) {
                    local = new IdGenerator();
                    instance = local;
                }
            }
        }
        return local;
    }

    public String nextId(String prefix) {
        String p = Validator.requireNonBlank(prefix, "prefix").toUpperCase();
        return p + "-" + counter.getAndIncrement();
    }
}


