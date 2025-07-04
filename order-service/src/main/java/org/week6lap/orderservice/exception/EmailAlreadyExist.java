package org.week6lap.orderservice.exception;

public class EmailAlreadyExist extends ResourceNotFoundException {
    public EmailAlreadyExist(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
