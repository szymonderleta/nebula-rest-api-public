package pl.derleta.nebula.exceptions;

public class MissingHeaderException extends RuntimeException {
    public MissingHeaderException(String message) {
        super(message);
    }
}
