package models;

public class AdminStaff extends User {

    public AdminStaff(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber);
    }

    @Override
    public String getRole() {
        return "Administrative Staff";
    }

    @Override
    public String getProfileSummary() {
        return getName() + " - Administrative Staff";
    }
}
