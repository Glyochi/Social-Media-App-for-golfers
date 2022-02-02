package com.example.springboot.Models.Exception.GroupException;

public class NoAdminException extends Exception{


    public NoAdminException(String message) {
        super(message);
    }

    public NoAdminException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAdminException(Throwable cause) {
        super(cause);
    }
}