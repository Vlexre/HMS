package gui;

import models.Prescription;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class DoctorPrescriptionsFrame extends JFrame {

    private final User doctor;
    private DefaultTableModel model;

    private JTextField patientIdField;
    private JTextField medicationField;
    private JTextField dosageField;
    private JTextField instructionsField;

    public DoctorPrescriptionsFrame(User doctor) {
        this.doctor = doctor;

        setTitle("Prescriptions - Dr. " + doctor.getName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Issue New Prescription"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Patient ID:"), gbc);
        patientIdField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(patientIdField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        form.add(new JLabel("Medication:"), gbc);
        medicationField = new JTextField(12);
        gbc.gridx = 3; gbc.gridy = 0;
        form.add(medicationField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Dosage:"), gbc);
        dosageField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 1;
        form.add(dosageField, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        form.add(new JLabel("Instructions:"), gbc);
        instructionsField = new JTextField(12);
        gbc.gridx = 3; gbc.gridy = 1;
        form.add(instructionsField, gbc);

        JButton issueButton = new JButton("Issue Prescription");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        form.add(issueButton, gbc);
        issueButton.addActionListener(e -> handleIssue());

        return form;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Rx ID", "Patient ID", "Medication", "Dosage", "Instructions", "Date"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private void handleIssue() {
        String patientId = patientIdField.getText().trim();
        String medication = medicationField.getText().trim();
        String dosage = dosageField.getText().trim();
        String instructions = instructionsField.getText().trim();

        if (patientId.isEmpty() || medication.isEmpty() || dosage.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Patient ID, medication, and dosage are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean patientExists = false;
        for (Account account : UserStore.getAll()) {
            if (account.getUser().getUserId().equalsIgnoreCase(patientId)
                    && account.getUser() instanceof models.Patient) {
                patientExists = true;
                break;
            }
        }
        if (!patientExists) {
            JOptionPane.showMessageDialog(this,
                    "No patient found with ID \"" + patientId + "\".",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = PrescriptionStore.generateNextId();
        String date = LocalDate.now().toString();
        Prescription prescription = new Prescription(
                id, patientId, doctor.getUserId(), medication, dosage, instructions, date);

        PrescriptionStore.add(prescription);

        patientIdField.setText("");
        medicationField.setText("");
        dosageField.setText("");
        instructionsField.setText("");

        refreshTable();
        JOptionPane.showMessageDialog(this, "Prescription " + id + " issued.");
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Prescription p : PrescriptionStore.getAll()) {
            if (p.getDoctorId().equalsIgnoreCase(doctor.getUserId())) {
                model.addRow(new Object[]{
                        p.getPrescriptionId(), p.getPatientId(), p.getMedication(),
                        p.getDosage(), p.getInstructions(), p.getDateIssued()
                });
            }
        }
    }
}