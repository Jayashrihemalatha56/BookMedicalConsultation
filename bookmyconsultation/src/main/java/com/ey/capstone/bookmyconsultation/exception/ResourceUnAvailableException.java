package com.ey.capstone.bookmyconsultation.exception;

public class ResourceUnAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceUnAvailableException() {
        super("Requested resource is unavailable");
    }

    public ResourceUnAvailableException(String message) {
        super(message);
    }

    public ResourceUnAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}