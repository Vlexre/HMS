package models;

public class Patient extends User {

    private String dateOfBirth;
    private String medicalRecordNumber;

    public Patient(String userId, String name, String email, String phoneNumber,
                   String dateOfBirth, String medicalRecordNumber) {
        super(userId, name, email, phoneNumber);
        this.dateOfBirth = dateOfBirth;
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String getProfileSummary() {
        return getName() + " - Patient - MRN: " + medicalRecordNumber;
    }
}
