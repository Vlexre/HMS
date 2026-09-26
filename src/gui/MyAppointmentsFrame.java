package gui;

import models.Appointment;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MyAppointmentsFrame extends JFrame {

    private final User patient;
    private DefaultTableModel model;
    private JTable table;

    public MyAppointmentsFrame(User patient) {
        this.patient = patient;

        setTitle("My Appointments");
        setSize(600, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Appointment ID", "Doctor ID", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton rescheduleButton = new JButton("Reschedule Selected");
        JButton cancelButton = new JButton("Cancel Selected");
        buttonPanel.add(rescheduleButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        rescheduleButton.addActionListener(e -> handleReschedule());
        cancelButton.addActionListener(e -> handleCancel());

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "You have no appointments yet.");
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Appointment appointment : AppointmentStore.getAll()) {
            if (appointment.getPatientId().equalsIgnoreCase(patient.getUserId())) {
                model.addRow(new Object[]{
                    appointment.getAppointmentId(), appointment.getDoctorId(),
                    appointment.getDate(), appointment.getTime(), appointment.getStatus()
                });
            }
        }
    }

    private String getSelectedAppointmentId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return null;
        }
        return (String) model.getValueAt(row, 0);
    }

    private void handleReschedule() {
        String appointmentId = getSelectedAppointmentId();
        if (appointmentId == null) {
            return;
        }

        String newDate = JOptionPane.showInputDialog(this, "New date (yyyy-MM-dd):");
        if (newDate == null) {
            return;
        }
        newDate = newDate.trim();

        if (!isValidDate(newDate)) {
            JOptionPane.showMessageDialog(this,
                    "Date must be in yyyy-MM-dd format (e.g. 2026-09-27).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newTime = JOptionPane.showInputDialog(this, "New time (HH:mm):");
        if (newTime == null) {
            return;
        }
        newTime = newTime.trim();

        if (!newTime.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            JOptionPane.showMessageDialog(this,
                    "Time must be in HH:mm 24-hour format (e.g. 14:30).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AppointmentStore.updateSchedule(appointmentId, newDate, newTime);
        refreshTable();
        JOptionPane.showMessageDialog(this, "Appointment rescheduled. Status set back to Pending.");
    }

    private void handleCancel() {
        String appointmentId = getSelectedAppointmentId();
        if (appointmentId == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel appointment " + appointmentId + "?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            AppointmentStore.updateStatus(appointmentId, "Cancelled");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment " + appointmentId + " has been cancelled.");
        }

    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
