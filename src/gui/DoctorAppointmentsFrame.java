package gui;

import models.Appointment;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DoctorAppointmentsFrame extends JFrame {

    public DoctorAppointmentsFrame(User doctor) {
        setTitle("My Appointments - Dr. " + doctor.getName());
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Appointment ID", "Patient ID", "Date", "Time", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Appointment> all = AppointmentStore.getAll();
        for (Appointment appointment : all) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctor.getUserId())) {
                model.addRow(new Object[]{
                        appointment.getAppointmentId(),
                        appointment.getPatientId(),
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getStatus()
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(scrollPane, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No appointments found for this doctor yet.");
        }
    }
}