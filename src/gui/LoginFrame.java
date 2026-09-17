package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import models.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;

    public LoginFrame() {
        setTitle("Hospital Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("HMS Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(roleLabel, gbc);

        String[] roles = {"Admin Staff", "Medical Manager", "Doctor", "Patient"};
        roleDropdown = new JComboBox<>(roles);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(roleDropdown, gbc);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(userLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(this::handleLogin);

        JButton registerButton = new JButton("Create New Account");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        registerButton.addActionListener(e -> new RegisterFrame().setVisible(true));

        add(panel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedRole = (String) roleDropdown.getSelectedItem();

        // --- Basic input validation (frontend responsibility) ---
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: replace this stub with real lookup once file-based
        // user storage (Kaung's part) is ready. For now this just
        // demonstrates routing to the right dashboard by role.
        Account account = UserStore.findByUsername(username, password);

        if (account == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please register first if you don't have an account.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User loggedInUser = account.getUser();
        JOptionPane.showMessageDialog(this,
                "Welcome, " + loggedInUser.getProfileSummary());

        DashboardFrame dashboard = new DashboardFrame(loggedInUser);
        dispose();
    }

}
