package models;

public class Doctor extends User {

    private String specialization;
    private String licenseNumber;

    public Doctor(String userId, String name, String email, String phoneNumber,
                  String specialization, String licenseNumber) {
        super(userId, name, email, phoneNumber);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String getProfileSummary() {
        return getName() + " - Doctor - " + specialization;
    }
}
