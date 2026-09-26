package persistence;

public class InvalidRatingDataException extends AccountException {
    public InvalidRatingDataException(String message) {
        super(message);
    }
}