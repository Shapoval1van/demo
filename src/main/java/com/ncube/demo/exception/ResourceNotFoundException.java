package com.ncube.demo.exception;

public class ResourceNotFoundException extends Exception {
    private final static String MESSAGE = "Member: %s not found";

    public ResourceNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }
}
