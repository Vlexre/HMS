package models;

public class MedicalManager extends User {

    private String department;

    public MedicalManager(String userId, String name, String email, String phoneNumber,
                           String department) {
        super(userId, name, email, phoneNumber);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getRole() {
        return "Medical Manager";
    }

    @Override
    public String getProfileSummary() {
        return getName() + " - Medical Manager - " + department;
    }
}
