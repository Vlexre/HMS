package persistence;

public class InvalidBillingDataException extends AccountException {
    public InvalidBillingDataException(String message) {
        super(message);
    }
}