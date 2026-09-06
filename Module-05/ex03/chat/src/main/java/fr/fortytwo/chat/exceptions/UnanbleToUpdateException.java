package fr.fortytwo.chat.exceptions;

public class UnanbleToUpdateException extends RuntimeException {
    public UnanbleToUpdateException() {
        super();
    }

    public UnanbleToUpdateException(String message) {
        super(message);
    }

    public UnanbleToUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnanbleToUpdateException(Throwable cause) {
        super(cause);
    }
}
