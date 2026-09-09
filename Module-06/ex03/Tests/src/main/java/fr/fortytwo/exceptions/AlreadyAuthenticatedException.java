package fr.fortytwo.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {

    public AlreadyAuthenticatedException(String message) {
        super(message);
    }
}
