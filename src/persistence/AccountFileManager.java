package persistence;

import gui.Account;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class AccountFileManager {

    // Format per line: username,password,role,userId,name,email,phone,extra1,extra2
    // extra1/extra2 depend on role (see buildExtraFields / parseExtraFields below)
    public void saveAccounts(List<Account> accounts, String filePath) throws AccountFileException {
        BufferedWriter writer = null;

        try {
            java.io.File file = new java.io.File(filePath);
            java.io.File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            writer = new BufferedWriter(new FileWriter(filePath));
            for (Account account : accounts) {
                User user = account.getUser();
                String line = account.getUsername() + "," + account.getPassword() + ","
                        + user.getRole() + "," + user.getUserId() + "," + user.getName() + ","
                        + user.getEmail() + "," + user.getPhoneNumber() + "," + buildExtraFields(user);
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new AccountFileException("Could not save accounts to " + filePath);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore close error
                }
            }
        }
    }

    public List<Account> loadAccounts(String filePath) throws AccountFileException, InvalidAccountDataException {
        List<Account> accounts = new ArrayList<Account>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;

            while (line != null) {
                lineNumber++;

                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);

                    if (parts.length < 8) {
                        throw new InvalidAccountDataException("Line " + lineNumber + " is missing data: " + line);
                    }

                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();
                    String userId = parts[3].trim();
                    String name = parts[4].trim();
                    String email = parts[5].trim();
                    String phone = parts[6].trim();
                    String extra1 = parts[7].trim();
                    String extra2 = parts.length > 8 ? parts[8].trim() : "";

                    if (username.equals("") || password.equals("")) {
                        throw new InvalidAccountDataException("Line " + lineNumber + " has an empty field: " + line);
                    }

                    User user = buildUser(role, userId, name, email, phone, extra1, extra2);
                    if (user == null) {
                        throw new InvalidAccountDataException("Line " + lineNumber + " has an unknown role: " + line);
                    }

                    accounts.add(new Account(username, password, user));
                }

                line = reader.readLine();
            }

        } catch (IOException e) {
            // file might not exist yet on first run, just return empty list
            return accounts;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore close error
                }
            }
        }

        return accounts;
    }

    private String buildExtraFields(User user) {
        if (user instanceof MedicalManager) {
            return ((MedicalManager) user).getDepartment();
        } else if (user instanceof Doctor) {
            Doctor d = (Doctor) user;
            return d.getSpecialization() + "," + d.getLicenseNumber();
        } else if (user instanceof Patient) {
            Patient p = (Patient) user;
            return p.getDateOfBirth() + "," + p.getMedicalRecordNumber();
        }
        return "";
    }

    private User buildUser(String role, String userId, String name, String email,
            String phone, String extra1, String extra2) {
        switch (role) {
            case "Administrative Staff":
                return new AdminStaff(userId, name, email, phone);
            case "Medical Manager":
                return new MedicalManager(userId, name, email, phone, extra1);
            case "Doctor":
                return new Doctor(userId, name, email, phone, extra1, extra2);
            case "Patient":
                return new Patient(userId, name, email, phone, extra1, extra2);
            default:
                return null;
        }
    }
}
