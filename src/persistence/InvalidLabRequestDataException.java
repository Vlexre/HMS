package persistence;

public class InvalidLabRequestDataException extends AccountException {
    public InvalidLabRequestDataException(String message) {
        super(message);
    }
}