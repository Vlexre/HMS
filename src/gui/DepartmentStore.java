package gui;

import java.util.ArrayList;
import java.util.List;
import models.Department;
import persistence.DepartmentFileException;
import persistence.DepartmentFileManager;
import persistence.InvalidDepartmentDataException;

public class DepartmentStore {

    private static final String FILE_PATH = "data/departments.txt";
    private static final DepartmentFileManager fileManager = new DepartmentFileManager();
    private static final ArrayList<Department> departments = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<Department> loaded = fileManager.loadDepartments(FILE_PATH);
            departments.addAll(loaded);
            for (Department d : departments) {
                String id = d.getDepartmentId();
                if (id != null && id.startsWith("DEP")) {
                    try {
                        int num = Integer.parseInt(id.substring(3));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (DepartmentFileException | InvalidDepartmentDataException ex) {
            System.err.println("Could not load departments: " + ex.getMessage());
        }
    }

    public static void add(Department department) {
        departments.add(department);
        persist();
    }

    public static void update(String departmentId, String newName, String newDescription) {
        for (Department d : departments) {
            if (d.getDepartmentId().equalsIgnoreCase(departmentId)) {
                d.setName(newName);
                d.setDescription(newDescription);
                break;
            }
        }
        persist();
    }

    public static void delete(String departmentId) {
        departments.removeIf(d -> d.getDepartmentId().equalsIgnoreCase(departmentId));
        persist();
    }

    public static ArrayList<Department> getAll() {
        return departments;
    }

    public static String generateNextId() {
        return "DEP" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.saveDepartments(departments, FILE_PATH);
        } catch (DepartmentFileException ex) {
            System.err.println("Could not save departments: " + ex.getMessage());
        }
    }
}