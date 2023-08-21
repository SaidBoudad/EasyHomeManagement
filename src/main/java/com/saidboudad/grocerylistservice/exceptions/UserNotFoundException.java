package com.saidboudad.grocerylistservice.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long clientId) {
        super("User with ID " + clientId + " not found.");
    }
}
