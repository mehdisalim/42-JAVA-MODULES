package fr.fortytwo.chat.exceptions;

public class UnanbleToSaveException extends RuntimeException {
    public UnanbleToSaveException() {
        super();
    }

    public UnanbleToSaveException(String message) {
        super(message);
    }

    public UnanbleToSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnanbleToSaveException(Throwable cause) {
        super(cause);
    }
}
