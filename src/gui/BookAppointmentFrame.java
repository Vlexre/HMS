package gui;

import models.Appointment;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookAppointmentFrame extends JFrame {

    private final User patient;
    private JTextField doctorIdField;
    private JTextField dateField;
    private JTextField timeField;

    public BookAppointmentFrame(User patient) {
        this.patient = patient;

        setTitle("Book Appointment");
        setSize(380, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("New Appointment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Doctor ID:"), gbc);
        doctorIdField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(doctorIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        dateField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Time (HH:mm):"), gbc);
        timeField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(timeField, gbc);

        JButton bookButton = new JButton("Book");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(bookButton, gbc);

        bookButton.addActionListener(e -> handleBooking());

        add(panel);
    }

    private void handleBooking() {
        String doctorId = doctorIdField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        // --- Input validation ---
        if (doctorId.isEmpty() || date.isEmpty() || time.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!isValidDate(date)) {
            showError("Date must be in yyyy-MM-dd format (e.g. 2026-09-27).");
            return;
        }

        if (!time.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            showError("Time must be in HH:mm 24-hour format (e.g. 14:30).");
            return;
        }

        String appointmentId = AppointmentStore.generateNextId();
        Appointment appointment = new Appointment(
                appointmentId, patient.getUserId(), doctorId, date, time, "Pending");

        AppointmentStore.add(appointment);

        JOptionPane.showMessageDialog(this,
                "Appointment " + appointmentId + " booked successfully.");
        dispose();
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }
}