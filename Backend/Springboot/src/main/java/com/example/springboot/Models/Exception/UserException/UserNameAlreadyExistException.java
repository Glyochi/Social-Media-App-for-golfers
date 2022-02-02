package com.example.springboot.Models.Exception.UserException;

public class UserNameAlreadyExistException extends RuntimeException{
    public UserNameAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}