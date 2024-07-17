package com.rsr.order_microservice.utils.exceptions;

public class UnknownProductIdException extends RuntimeException {
    public UnknownProductIdException() {
        super("Could not find the passed id. Such Element does not exist.");
    }
}
