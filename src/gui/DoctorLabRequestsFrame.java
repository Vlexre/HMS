package gui;

import models.LabRequest;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class DoctorLabRequestsFrame extends JFrame {

    private final User doctor;
    private DefaultTableModel model;

    private JTextField patientIdField;
    private JTextField testTypeField;

    public DoctorLabRequestsFrame(User doctor) {
        this.doctor = doctor;

        setTitle("Lab / Imaging Requests");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("New Lab / Imaging Request"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Patient ID:"), gbc);
        patientIdField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(patientIdField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        form.add(new JLabel("Test Type:"), gbc);
        testTypeField = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0;
        form.add(testTypeField, gbc);

        JButton submitButton = new JButton("Submit Request");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        form.add(submitButton, gbc);
        submitButton.addActionListener(e -> handleSubmit());

        return form;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Request ID", "Patient ID", "Test Type", "Status", "Date"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private void handleSubmit() {
        String patientId = patientIdField.getText().trim();
        String testType = testTypeField.getText().trim();

        if (patientId.isEmpty() || testType.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Patient ID and test type are required.",
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

        String id = LabRequestStore.generateNextId();
        String date = LocalDate.now().toString();
        LabRequest request = new LabRequest(id, patientId, doctor.getUserId(), testType, "Pending", date);
        LabRequestStore.add(request);

        patientIdField.setText("");
        testTypeField.setText("");
        refreshTable();
        JOptionPane.showMessageDialog(this, "Request " + id + " submitted.");
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (LabRequest r : LabRequestStore.getAll()) {
            if (r.getDoctorId().equalsIgnoreCase(doctor.getUserId())) {
                model.addRow(new Object[]{
                        r.getRequestId(), r.getPatientId(), r.getTestType(),
                        r.getStatus(), r.getDateRequested()
                });
            }
        }
    }
}