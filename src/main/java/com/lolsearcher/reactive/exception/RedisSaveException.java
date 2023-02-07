package com.lolsearcher.reactive.exception;

public class RedisSaveException extends RuntimeException{

    private String message;

    public RedisSaveException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
