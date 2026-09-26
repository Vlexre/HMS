package gui;

import models.*;
import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    public ReportsFrame() {
        setTitle("Reports");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("System Summary Report");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));

        int patients = 0, doctors = 0, admins = 0, managers = 0;
        for (Account account : UserStore.getAll()) {
            User user = account.getUser();
            if (user instanceof Patient) patients++;
            else if (user instanceof Doctor) doctors++;
            else if (user instanceof AdminStaff) admins++;
            else if (user instanceof MedicalManager) managers++;
        }

        int pending = 0, confirmed = 0, cancelled = 0;
        for (Appointment a : AppointmentStore.getAll()) {
            String status = a.getStatus() == null ? "" : a.getStatus();
            if (status.equalsIgnoreCase("Confirmed")) confirmed++;
            else if (status.equalsIgnoreCase("Cancelled")) cancelled++;
            else pending++;
        }

        panel.add(sectionLabel("Users"));
        panel.add(new JLabel("Total Patients: " + patients));
        panel.add(new JLabel("Total Doctors: " + doctors));
        panel.add(new JLabel("Total Admin Staff: " + admins));
        panel.add(new JLabel("Total Medical Managers: " + managers));
        panel.add(Box.createVerticalStrut(15));

        panel.add(sectionLabel("Appointments"));
        panel.add(new JLabel("Total: " + AppointmentStore.getAll().size()));
        panel.add(new JLabel("Pending: " + pending));
        panel.add(new JLabel("Confirmed: " + confirmed));
        panel.add(new JLabel("Cancelled: " + cancelled));
        panel.add(Box.createVerticalStrut(15));

        panel.add(sectionLabel("Prescriptions"));
        panel.add(new JLabel("Total Issued: " + PrescriptionStore.getAll().size()));

        add(panel);
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }
}