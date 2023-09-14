package uk.co.huntersix.spring.rest.exception;

public class PersonNotFoundException extends RuntimeException {

    private String message;

    public PersonNotFoundException(String message) {
        super(message);

    }
}
