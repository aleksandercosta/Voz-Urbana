package com.example.VozUrbana.services.exceptions;

public class DataBindingViolationException extends RuntimeException {

    public DataBindingViolationException(String message) {
        super(message);
    }

    public DataBindingViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}