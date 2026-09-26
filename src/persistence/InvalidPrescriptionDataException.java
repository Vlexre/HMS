package persistence;

public class InvalidPrescriptionDataException extends AccountException {
    public InvalidPrescriptionDataException(String message) {
        super(message);
    }
}