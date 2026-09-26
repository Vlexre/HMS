package persistence;

import models.Department;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentFileManager {

    // Format per line: departmentId,name,description
    public void saveDepartments(List<Department> departments, String filePath)
            throws DepartmentFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (Department d : departments) {
                String line = d.getDepartmentId() + "," + d.getName() + "," + d.getDescription();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DepartmentFileException("Could not save departments to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<Department> loadDepartments(String filePath)
            throws DepartmentFileException, InvalidDepartmentDataException {
        List<Department> departments = new ArrayList<Department>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 3) {
                        throw new InvalidDepartmentDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String description = parts[2].trim();

                    if (id.equals("") || name.equals("")) {
                        throw new InvalidDepartmentDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    departments.add(new Department(id, name, description));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return departments;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return departments;
    }
}