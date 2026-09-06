package fr.fortytwo.numbers;

/**
 * IllegalNumberException
 */
public class IllegalNumberException extends RuntimeException {

    static private final String MESSAGE = "Illegal number exception";

    public IllegalNumberException() {
        super(MESSAGE);
    }

    public IllegalNumberException(final Exception e) {
        super(MESSAGE, e);
    }

}
