package models;

public class Department {

    private String departmentId;
    private String name;
    private String description;

    public Department(String departmentId, String name, String description) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }

    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return departmentId + " - " + name;
    }
}