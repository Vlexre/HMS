package gui;

import models.Billing;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminBillingFrame extends JFrame {

    private static final Map<String, Double> GRADE_PRICES = new LinkedHashMap<>();
    static {
        GRADE_PRICES.put("Standard Consultation", 50.0);
        GRADE_PRICES.put("Specialist Consultation", 100.0);
        GRADE_PRICES.put("Follow-up Visit", 30.0);
        GRADE_PRICES.put("Emergency Visit", 150.0);
        GRADE_PRICES.put("Surgery", 500.0);
    }

    private DefaultTableModel model;
    private JTable table;

    private JTextField patientIdField;
    private JTextField doctorIdField;
    private JComboBox<String> gradeDropdown;
    private JTextField amountField;
    private JCheckBox insuranceCheckBox;
    private JComboBox<String> networkDropdown;

    public AdminBillingFrame() {
        setTitle("Billing / Medical Grading");
        setSize(750, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Issue New Bill"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Patient ID:"), gbc);
        patientIdField = new JTextField(8);
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(patientIdField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        form.add(new JLabel("Doctor ID:"), gbc);
        doctorIdField = new JTextField(8);
        gbc.gridx = 3; gbc.gridy = 0;
        form.add(doctorIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Medical Grade:"), gbc);
        gradeDropdown = new JComboBox<>(ConfigStore.getRates().keySet().toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 1;
        form.add(gradeDropdown, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        form.add(new JLabel("Amount ($):"), gbc);
        
        double initialAmount = 0.0;
        if (gradeDropdown.getItemCount() > 0) {
            initialAmount = ConfigStore.getRates().get(gradeDropdown.getItemAt(0));
        }
        amountField = new JTextField(String.valueOf(initialAmount), 8);
        gbc.gridx = 3; gbc.gridy = 1;
        form.add(amountField, gbc);

        gradeDropdown.addActionListener(e -> {
            Double rate = ConfigStore.getRates().get((String) gradeDropdown.getSelectedItem());
            if (rate != null) amountField.setText(String.valueOf(rate));
        });

        insuranceCheckBox = new JCheckBox("Covered by Insurance");
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(insuranceCheckBox, gbc);

        networkDropdown = new JComboBox<>(ConfigStore.getNetworks().toArray(new String[0]));
        networkDropdown.setEnabled(false);
        gbc.gridx = 1; gbc.gridy = 2;
        form.add(networkDropdown, gbc);

        insuranceCheckBox.addActionListener(e -> networkDropdown.setEnabled(insuranceCheckBox.isSelected()));

        JButton issueButton = new JButton("Issue Bill");
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(issueButton, gbc);
        issueButton.addActionListener(e -> handleIssue());

        return form;
    }

    private JScrollPane buildTable() {
        String[] columns = {"Bill ID", "Patient ID", "Doctor ID", "Grade", "Amount", "Insurance", "Amount Due", "Status", "Date"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        TableUtils.enableCellPopup(table, this);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return scrollPane;
    }

    private JPanel buildActions() {
        JPanel panel = new JPanel();
        JButton markPaidButton = new JButton("Mark Selected as Paid");
        panel.add(markPaidButton);
        markPaidButton.addActionListener(e -> handleMarkPaid());
        return panel;
    }

    private void handleIssue() {
        String patientId = patientIdField.getText().trim();
        String doctorId = doctorIdField.getText().trim();
        String grade = (String) gradeDropdown.getSelectedItem();
        String amountText = amountField.getText().trim();
        boolean insuranceCovered = insuranceCheckBox.isSelected();
        String insuranceNetwork = insuranceCovered && networkDropdown.getSelectedItem() != null ? 
                (String) networkDropdown.getSelectedItem() : "None";

        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required.",
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

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount < 0) throw new NumberFormatException();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be a valid non-negative number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = BillingStore.generateNextId();
        String date = LocalDate.now().toString();
        String status = insuranceCovered ? "Paid" : "Unpaid";

        Billing billing = new Billing(id, patientId, doctorId, grade, amount, insuranceCovered, insuranceNetwork, status, date);
        BillingStore.add(billing);

        patientIdField.setText("");
        doctorIdField.setText("");
        insuranceCheckBox.setSelected(false);
        networkDropdown.setEnabled(false);
        refreshTable();
        JOptionPane.showMessageDialog(this, "Bill " + id + " issued.");
    }

    private void handleMarkPaid() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill first.");
            return;
        }
        String billingId = (String) model.getValueAt(row, 0);
        BillingStore.markPaid(billingId);
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Billing b : BillingStore.getAll()) {
            model.addRow(new Object[]{
                    b.getBillingId(), b.getPatientId(), b.getDoctorId(), b.getServiceType(),
                    String.format("$%.2f", b.getAmount()),
                    b.isInsuranceCovered() ? "Yes (" + b.getInsuranceNetwork() + ")" : "No",
                    String.format("$%.2f", b.getAmountDue()),
                    b.getStatus(), b.getDateIssued()
            });
        }
    }
}