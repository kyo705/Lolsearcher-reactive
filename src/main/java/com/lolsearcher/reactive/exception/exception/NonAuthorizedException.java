package com.lolsearcher.reactive.exception.exception;

public class NonAuthorizedException extends RuntimeException{

    private String message;

    public NonAuthorizedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
