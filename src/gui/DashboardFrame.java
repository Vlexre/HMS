package gui;

import java.awt.*;
import javax.swing.*;
import models.*;

public class DashboardFrame extends JFrame {

    private User currentUser;

    public DashboardFrame(User user) {
        this.currentUser = user;

        setTitle("HMS Dashboard - " + currentUser.getRole());
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 90, 150));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(welcomeLabel);

        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String item : getMenuItemsForRole()) {
            JButton button = new JButton(item);
            button.addActionListener(e
                    -> JOptionPane.showMessageDialog(this, item + " screen coming soon."));
            sidebar.add(button);
        }

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(evt -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutButton);

        return sidebar;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea summary = new JTextArea(currentUser.getProfileSummary());
        summary.setFont(new Font("SansSerif", Font.PLAIN, 14));
        summary.setEditable(false);
        summary.setLineWrap(true);

        content.add(summary, BorderLayout.NORTH);
        return content;
    }

    // Menu items differ per role — this is where role-based
    // permissions from the assignment brief get reflected in the UI.
    private String[] getMenuItemsForRole() {
        switch (currentUser.getRole()) {
            case "Administrative Staff":
                return new String[]{"Manage Users", "View Appointments", "Reports"};
            case "Medical Manager":
                return new String[]{"Manage Doctors", "Department Overview", "Reports"};
            case "Doctor":
                return new String[]{"My Appointments", "Patient Records", "Prescriptions"};
            case "Patient":
                return new String[]{"Book Appointment", "My Records", "My Prescriptions"};
            default:
                return new String[]{};
        }
    }
}
