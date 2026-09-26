package gui;

import java.awt.*;
import javax.swing.*;
import models.*;

public class DashboardFrame extends JFrame {

    private Account currentAccount;
    private User currentUser;

    private JLabel welcomeLabel;
    private JTextArea summaryArea;

    public DashboardFrame(Account account) {
        this.currentAccount = account;
        this.currentUser = account.getUser();

        setTitle("HMS Dashboard - " + currentUser.getRole());
        setSize(700, 680);
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

        welcomeLabel = new JLabel(
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
            button.addActionListener(e -> handleMenuClick(item));
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

        summaryArea = new JTextArea(currentUser.getProfileSummary());
        summaryArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);

        content.add(summaryArea, BorderLayout.NORTH);
        return content;
    }

    private String[] getMenuItemsForRole() {
        switch (currentUser.getRole()) {
            case "Administrative Staff":
                return new String[]{"Manage Users", "View Appointments", "Reports", "Lab Requests", "Billing", "Billing Config", "Edit Profile"};
            case "Medical Manager":
                return new String[]{"Manage Doctors", "Manage Departments", "Reports", "Edit Profile"};
            case "Doctor":
                return new String[]{"My Appointments", "Patient Records", "Prescriptions", "Lab Requests", "Edit Profile"};
            case "Patient":
                return new String[]{"Book Appointment", "My Appointments", "My Records",
                        "My Prescriptions", "My Consultation History", "My Bills", "Rate Doctor", "Edit Profile"};
            default:
                return new String[]{};
        }
    }

    private void handleMenuClick(String item) {
        switch (item) {
            case "Book Appointment":
                new BookAppointmentFrame(currentUser).setVisible(true);
                break;
            case "My Records":
                new MyRecordsFrame(currentUser).setVisible(true);
                break;
            case "My Prescriptions":
                new MyPrescriptionsFrame(currentUser).setVisible(true);
                break;
            case "My Appointments":
                // Patients get their own bookable/cancellable list;
                // Doctors get the read-only schedule view.
                if (currentUser instanceof Patient) {
                    new MyAppointmentsFrame(currentUser).setVisible(true);
                } else {
                    new DoctorAppointmentsFrame(currentUser).setVisible(true);
                }
                break;
            case "Rate Doctor":
                new RateVisitFrame(currentUser).setVisible(true);
                break;
            case "My Consultation History":
                new ConsultationHistoryFrame(currentUser.getUserId()).setVisible(true);
                break;
            case "Billing":
                new AdminBillingFrame().setVisible(true);
                break;
            case "Billing Config":
                new AdminBillingConfigFrame().setVisible(true);
                break;
            case "My Bills":
                new MyBillsFrame(currentUser).setVisible(true);
                break;
            case "Manage Users":
                new ManageUsersFrame().setVisible(true);
                break;
            case "View Appointments":
                new AllAppointmentsFrame().setVisible(true);
                break;
            case "Reports":
                new ReportsFrame().setVisible(true);
                break;
            case "Manage Doctors":
                new ManageDoctorsFrame().setVisible(true);
                break;
            case "Manage Departments":
                new DepartmentOverviewFrame().setVisible(true);
                break;
            case "Patient Records":
                new PatientRecordsFrame(currentUser).setVisible(true);
                break;
            case "Prescriptions":
                new DoctorPrescriptionsFrame(currentUser).setVisible(true);
                break;
            case "Lab Requests":
                if (currentUser instanceof Doctor) {
                    new DoctorLabRequestsFrame(currentUser).setVisible(true);
                } else {
                    new AdminLabRequestsFrame().setVisible(true);
                }
                break;
            case "Edit Profile":
                new EditUserFrame(currentAccount, this::refreshUserData).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, item + " screen coming soon.");
        }
    }

    // Called after Edit Profile saves changes, so the header/summary
    // reflect the update immediately without needing to log out and back in.
    private void refreshUserData() {
        Account refreshed = UserStore.findAccountByUsername(currentAccount.getUsername());
        if (refreshed != null) {
            currentAccount = refreshed;
            currentUser = refreshed.getUser();
            welcomeLabel.setText("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
            summaryArea.setText(currentUser.getProfileSummary());
        }
    }
}