package gui;

import models.*;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JComboBox<String> roleDropdown;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;

    // Role-specific fields
    private final JPanel extraFieldsPanel;
    private final CardLayout cardLayout;

    private final JTextField departmentField;      // Medical Manager
    private final JTextField specializationField;  // Doctor
    private final JTextField licenseField;          // Doctor
    private final JTextField dobField;               // Patient
    private final JTextField mrnField;               // Patient

    public RegisterFrame() {
        setTitle("Register New Account");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        String[] roles = {"Admin Staff", "Medical Manager", "Doctor", "Patient"};
        roleDropdown = new JComboBox<>(roles);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(roleDropdown, gbc);
        row++;

        usernameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(usernameField, gbc);
        row++;

        passwordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(passwordField, gbc);
        row++;

        nameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(nameField, gbc);
        row++;

        emailField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(emailField, gbc);
        row++;

        phoneField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(phoneField, gbc);
        row++;

        // --- Role-specific fields, swapped via CardLayout ---
        cardLayout = new CardLayout();
        extraFieldsPanel = new JPanel(cardLayout);

        extraFieldsPanel.add(new JPanel(), "Admin Staff"); // no extra fields

        JPanel managerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        departmentField = new JTextField();
        managerPanel.add(new JLabel("Department:"));
        managerPanel.add(departmentField);
        extraFieldsPanel.add(managerPanel, "Medical Manager");

        JPanel doctorPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        specializationField = new JTextField();
        licenseField = new JTextField();
        doctorPanel.add(new JLabel("Specialization:"));
        doctorPanel.add(specializationField);
        doctorPanel.add(new JLabel("License No.:"));
        doctorPanel.add(licenseField);
        extraFieldsPanel.add(doctorPanel, "Doctor");

        JPanel patientPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        dobField = new JTextField();
        mrnField = new JTextField();
        patientPanel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        patientPanel.add(dobField);
        patientPanel.add(new JLabel("Medical Record No.:"));
        patientPanel.add(mrnField);
        extraFieldsPanel.add(patientPanel, "Patient");

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(extraFieldsPanel, gbc);
        gbc.gridwidth = 1;
        row++;

        roleDropdown.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                cardLayout.show(extraFieldsPanel, (String) roleDropdown.getSelectedItem());
            }
        });

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> handleRegister());

        add(panel);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = (String) roleDropdown.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()
                || email.isEmpty() || phone.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (UserStore.usernameExists(username)) {
            showError("This username is already taken.");
            return;
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (!phone.matches("\\d{7,15}")) {
            showError("Phone number must contain 7 to 15 digits only.");
            return;
        }

        User newUser = buildUserForRole(role, name, email, phone);
        if (newUser == null) {
            showError("Please fill in the role-specific fields.");
            return;
        }

        UserStore.add(new Account(username, password, newUser));

        JOptionPane.showMessageDialog(this,
                "Account created successfully. You can now log in.");
        dispose();
    }

    private User buildUserForRole(String role, String name, String email, String phone) {
        switch (role) {
            case "Admin Staff": {
                String id = UserStore.generateNextUserId("A");
                return new AdminStaff(id, name, email, phone);
            }
            case "Medical Manager": {
                String department = departmentField.getText().trim();
                if (department.isEmpty()) return null;
                String id = UserStore.generateNextUserId("M");
                return new MedicalManager(id, name, email, phone, department);
            }
            case "Doctor": {
                String specialization = specializationField.getText().trim();
                String license = licenseField.getText().trim();
                if (specialization.isEmpty() || license.isEmpty()) return null;
                String id = UserStore.generateNextUserId("D");
                return new Doctor(id, name, email, phone, specialization, license);
            }
            case "Patient": {
                String dob = dobField.getText().trim();
                String mrn = mrnField.getText().trim();
                if (dob.isEmpty() || mrn.isEmpty()) return null;
                String id = UserStore.generateNextUserId("P");
                return new Patient(id, name, email, phone, dob, mrn);
            }
            default:
                return null;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }
}