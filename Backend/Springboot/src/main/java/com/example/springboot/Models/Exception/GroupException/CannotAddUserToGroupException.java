package com.example.springboot.Models.Exception.GroupException;

public class CannotAddUserToGroupException extends RuntimeException{
    public CannotAddUserToGroupException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
