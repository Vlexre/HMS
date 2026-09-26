package gui;

import models.Patient;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientRecordsFrame extends JFrame {

    private final User doctor;
    private DefaultTableModel model;
    private JTable table;

    public PatientRecordsFrame(User doctor) {
        this.doctor = doctor;

        setTitle("Patient Records");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Patient ID", "Name", "Email", "Phone", "Date of Birth", "MRN"};
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
        JButton addNoteButton = new JButton("Add Consultation Note");
        JButton historyButton = new JButton("View History");
        buttonPanel.add(addNoteButton);
        buttonPanel.add(historyButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addNoteButton.addActionListener(e -> handleAddNote());
        historyButton.addActionListener(e -> handleViewHistory());

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No registered patients found yet.");
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Account account : UserStore.getAll()) {
            User user = account.getUser();
            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                model.addRow(new Object[]{
                        patient.getUserId(), patient.getName(), patient.getEmail(),
                        patient.getPhoneNumber(), patient.getDateOfBirth(), patient.getMedicalRecordNumber()
                });
            }
        }
    }

    private String getSelectedPatientId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return null;
        }
        return (String) model.getValueAt(row, 0);
    }

    private void handleAddNote() {
        String patientId = getSelectedPatientId();
        if (patientId == null) return;
        new AddConsultationNoteFrame(doctor, patientId, () -> {}).setVisible(true);
    }

    private void handleViewHistory() {
        String patientId = getSelectedPatientId();
        if (patientId == null) return;
        new ConsultationHistoryFrame(patientId).setVisible(true);
    }
}