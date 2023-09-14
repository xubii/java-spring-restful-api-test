package uk.co.huntersix.spring.rest.exception;

public class PersonAlreadyExistException extends RuntimeException {

    private String message;

    public PersonAlreadyExistException(String message) {
        super(message);

    }
}
