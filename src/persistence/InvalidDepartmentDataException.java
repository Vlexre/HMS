package persistence;

public class InvalidDepartmentDataException extends AccountException {
    public InvalidDepartmentDataException(String message) {
        super(message);
    }
}