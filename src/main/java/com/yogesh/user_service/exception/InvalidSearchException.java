package com.yogesh.user_service.exception;

public class InvalidSearchException extends RuntimeException{
    public  InvalidSearchException(String message){
        super(message);
    }
}
