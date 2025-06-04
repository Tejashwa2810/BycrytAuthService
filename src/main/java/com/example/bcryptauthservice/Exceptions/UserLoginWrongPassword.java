package com.example.bcryptauthservice.Exceptions;

public class UserLoginWrongPassword extends RuntimeException{
    public UserLoginWrongPassword(String message){
        super(message);
    }
}
