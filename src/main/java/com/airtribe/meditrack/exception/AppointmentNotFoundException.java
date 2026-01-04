package com.airtribe.meditrack.exception;

/**
 * Thrown when an appointment lookup fails.
 */
public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}


