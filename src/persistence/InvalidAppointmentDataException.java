package persistence;

public class InvalidAppointmentDataException extends AccountException {

    public InvalidAppointmentDataException(String message) {
        super(message);
    }
}