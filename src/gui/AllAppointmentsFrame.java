package gui;

import models.Appointment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AllAppointmentsFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public AllAppointmentsFrame() {
        setTitle("All Appointments");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Appointment ID", "Patient ID", "Doctor ID", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateStatusButton = new JButton("Update Status");
        buttonPanel.add(updateStatusButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateStatusButton.addActionListener(e -> handleUpdateStatus());
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Appointment appointment : AppointmentStore.getAll()) {
            model.addRow(new Object[]{
                    appointment.getAppointmentId(), appointment.getPatientId(),
                    appointment.getDoctorId(), appointment.getDate(),
                    appointment.getTime(), appointment.getStatus()
            });
        }
    }

    private void handleUpdateStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return;
        }

        String appointmentId = (String) model.getValueAt(row, 0);
        String[] options = {"Pending", "Confirmed", "Cancelled"};
        String newStatus = (String) JOptionPane.showInputDialog(this,
                "New status for " + appointmentId + ":", "Update Status",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (newStatus != null) {
            AppointmentStore.updateStatus(appointmentId, newStatus);
            refreshTable();
        }
    }
}