package org.week6lap.authservice.exception;

public class EmailAlreadyExist extends ResourceNotFoundException {
    public EmailAlreadyExist(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
