package com.airtribe.meditrack.exception;

/**
 * Thrown when validation fails for user input or entity state.
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}


