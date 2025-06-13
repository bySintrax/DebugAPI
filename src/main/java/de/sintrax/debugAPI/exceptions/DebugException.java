package de.sintrax.debugAPI.exceptions;

public class DebugException extends RuntimeException {
    public DebugException(String message) {
        super(message);
    }

    public DebugException(String message, Throwable cause) {
        super(message, cause);
    }
}