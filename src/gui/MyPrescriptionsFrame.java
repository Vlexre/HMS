package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Prescription;
import models.User;

public class MyPrescriptionsFrame extends JFrame {

    public MyPrescriptionsFrame(User patient) {
        setTitle("My Prescriptions");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Rx ID", "Doctor ID", "Medication", "Dosage", "Instructions", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Prescription p : PrescriptionStore.getAll()) {
            if (p.getPatientId().equalsIgnoreCase(patient.getUserId())) {
                model.addRow(new Object[]{
                        p.getPrescriptionId(), p.getDoctorId(), p.getMedication(),
                        p.getDosage(), p.getInstructions(), p.getDateIssued()
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(scrollPane, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "You have no prescriptions yet.");
        }
    }
}