package org.week6lap.restaurantservice.exception;

public class EmailAlreadyExist extends ResourceNotFoundException {
    public EmailAlreadyExist(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
