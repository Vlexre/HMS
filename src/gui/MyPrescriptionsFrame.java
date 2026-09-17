package gui;

import java.awt.*;
import javax.swing.*;

public class MyPrescriptionsFrame extends JFrame {

    public MyPrescriptionsFrame() {
        setTitle("My Prescriptions");
        setSize(380, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TODO: replace with a real prescriptions table once a
        // Prescription model exists in models/. Discuss with the
        // team who owns that class.
        JLabel placeholder = new JLabel(
                "<html><center>No Prescription model exists yet.<br>"
                + "This screen will list prescriptions once<br>"
                + "that class is added by the team.</center></html>",
                SwingConstants.CENTER);
        panel.add(placeholder, BorderLayout.CENTER);

        add(panel);
    }
}