package gui;

import models.LabRequest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminLabRequestsFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public AdminLabRequestsFrame() {
        setTitle("Lab / Imaging Requests - All");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Request ID", "Patient ID", "Doctor ID", "Test Type", "Status", "Date"};
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
        JButton markCompletedButton = new JButton("Mark Completed");
        buttonPanel.add(markCompletedButton);
        add(buttonPanel, BorderLayout.SOUTH);

        markCompletedButton.addActionListener(e -> handleMarkCompleted());
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (LabRequest r : LabRequestStore.getAll()) {
            model.addRow(new Object[]{
                    r.getRequestId(), r.getPatientId(), r.getDoctorId(),
                    r.getTestType(), r.getStatus(), r.getDateRequested()
            });
        }
    }

    private void handleMarkCompleted() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request first.");
            return;
        }

        String requestId = (String) model.getValueAt(row, 0);
        LabRequestStore.updateStatus(requestId, "Completed");
        refreshTable();
    }
}