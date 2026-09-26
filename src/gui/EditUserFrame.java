package gui;

import models.*;
import javax.swing.*;
import java.awt.*;

public class EditUserFrame extends JFrame {

    private final Account originalAccount;
    private final Runnable onSaved;

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextField extraField1;
    private final JTextField extraField2;
    private final String role;

    public EditUserFrame(Account account, Runnable onSaved) {
        this.originalAccount = account;
        this.onSaved = onSaved;
        User user = account.getUser();
        this.role = user.getRole();

        setTitle("Edit User - " + user.getName());
        setSize(380, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        JLabel titleLabel = new JLabel("Edit " + role, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(user.getName(), 15);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(nameField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(user.getEmail(), 15);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(emailField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(user.getPhoneNumber(), 15);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(phoneField, gbc);
        row++;

        String extra1Label = "";
        String extra1Value = "";
        String extra2Label = "";
        String extra2Value = "";

        if (user instanceof MedicalManager) {
            extra1Label = "Department:";
            extra1Value = ((MedicalManager) user).getDepartment();
        } else if (user instanceof Doctor) {
            Doctor d = (Doctor) user;
            extra1Label = "Specialization:";
            extra1Value = d.getSpecialization();
            extra2Label = "License No.:";
            extra2Value = d.getLicenseNumber();
        } else if (user instanceof Patient) {
            Patient p = (Patient) user;
            extra1Label = "Date of Birth:";
            extra1Value = p.getDateOfBirth();
            extra2Label = "Medical Record No.:";
            extra2Value = p.getMedicalRecordNumber();
        }

        extraField1 = new JTextField(extra1Value, 15);
        if (!extra1Label.isEmpty()) {
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel(extra1Label), gbc);
            gbc.gridx = 1; gbc.gridy = row;
            panel.add(extraField1, gbc);
            row++;
        }

        extraField2 = new JTextField(extra2Value, 15);
        if (!extra2Label.isEmpty()) {
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel(extra2Label), gbc);
            gbc.gridx = 1; gbc.gridy = row;
            panel.add(extraField2, gbc);
            row++;
        }

        JButton saveButton = new JButton("Save Changes");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(saveButton, gbc);
        saveButton.addActionListener(e -> handleSave());

        add(panel);
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, email, and phone cannot be empty.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must contain 7 to 15 digits only.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = originalAccount.getUser().getUserId();
        User updatedUser;

        switch (role) {
            case "Administrative Staff":
                updatedUser = new AdminStaff(userId, name, email, phone);
                break;
            case "Medical Manager":
                updatedUser = new MedicalManager(userId, name, email, phone, extraField1.getText().trim());
                break;
            case "Doctor":
                updatedUser = new Doctor(userId, name, email, phone,
                        extraField1.getText().trim(), extraField2.getText().trim());
                break;
            case "Patient":
                updatedUser = new Patient(userId, name, email, phone,
                        extraField1.getText().trim(), extraField2.getText().trim());
                break;
            default:
                updatedUser = originalAccount.getUser();
        }

        Account updatedAccount = new Account(
                originalAccount.getUsername(), originalAccount.getPassword(), updatedUser);

        UserStore.update(originalAccount.getUsername(), updatedAccount);

        JOptionPane.showMessageDialog(this, "User updated successfully.");
        onSaved.run();
        dispose();
    }
}