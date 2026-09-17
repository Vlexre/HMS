package gui;

import models.Patient;
import models.User;
import javax.swing.*;
import java.awt.*;

public class MyRecordsFrame extends JFrame {

    public MyRecordsFrame(User user) {
        setTitle("My Records");
        setSize(380, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Patient Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel);

        panel.add(new JLabel("Name: " + user.getName()));
        panel.add(new JLabel("Email: " + user.getEmail()));
        panel.add(new JLabel("Phone: " + user.getPhoneNumber()));

        // Patient-specific fields require casting since
        // User only exposes the shared fields above.
        if (user instanceof Patient) {
            Patient patient = (Patient) user;
            panel.add(new JLabel("Date of Birth: " + patient.getDateOfBirth()));
            panel.add(new JLabel("Medical Record No.: " + patient.getMedicalRecordNumber()));
        }

        add(panel);
    }
}