package com.example.bcryptauthservice.Exceptions;

public class UserNotRegisteredException extends RuntimeException {
    public UserNotRegisteredException(String email) {
        super("User is not Registered with this email: " + email);
    }
}
