package com.yogesh.user_service.exception;

public class InvalidPaginationException extends RuntimeException{
    public InvalidPaginationException(String message) {
        super(message);
    }
}
