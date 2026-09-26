package gui;

import models.Billing;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyBillsFrame extends JFrame {

    public MyBillsFrame(User patient) {
        setTitle("My Bills");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Bill ID", "Grade", "Amount", "Insurance", "Amount Due", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Billing b : BillingStore.getAll()) {
            if (b.getPatientId().equalsIgnoreCase(patient.getUserId())) {
                model.addRow(new Object[]{
                        b.getBillingId(), b.getServiceType(),
                        String.format("$%.2f", b.getAmount()),
                        b.isInsuranceCovered() ? "Yes (" + b.getInsuranceNetwork() + ")" : "No",
                        String.format("$%.2f", b.getAmountDue()),
                        b.getStatus(), b.getDateIssued()
                });
            }
        }

        JTable table = new JTable(model);
        TableUtils.enableCellPopup(table, this);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(scrollPane, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "You have no bills yet.");
        }
    }
}