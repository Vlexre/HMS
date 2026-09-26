package gui;

import models.Department;
import models.Doctor;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DepartmentOverviewFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public DepartmentOverviewFrame() {
        setTitle("Manage Departments");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Department ID", "Name", "Description", "Doctors Assigned"};
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
        JButton addButton = new JButton("Add Department");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
    }

    private Map<String, Integer> countDoctorsByDepartmentName() {
        Map<String, Integer> counts = new HashMap<>();
        for (Account account : UserStore.getAll()) {
            User user = account.getUser();
            if (user instanceof Doctor) {
                String specialization = ((Doctor) user).getSpecialization();
                counts.merge(specialization, 1, Integer::sum);
            }
        }
        return counts;
    }

    private void refreshTable() {
        model.setRowCount(0);
        Map<String, Integer> counts = countDoctorsByDepartmentName();
        for (Department d : DepartmentStore.getAll()) {
            int doctorCount = counts.getOrDefault(d.getName(), 0);
            model.addRow(new Object[]{d.getDepartmentId(), d.getName(), d.getDescription(), doctorCount});
        }
    }

    private void handleAdd() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        Object[] message = {
                "Department Name:", nameField,
                "Description:", descField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Add Department",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department name is required.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = DepartmentStore.generateNextId();
            DepartmentStore.add(new Department(id, name, description));
            refreshTable();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department first.");
            return;
        }

        String id = (String) model.getValueAt(row, 0);
        String currentName = (String) model.getValueAt(row, 1);
        String currentDesc = (String) model.getValueAt(row, 2);

        JTextField nameField = new JTextField(currentName);
        JTextField descField = new JTextField(currentDesc);
        Object[] message = {
                "Department Name:", nameField,
                "Description:", descField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Edit Department",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department name is required.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DepartmentStore.update(id, name, description);
            refreshTable();
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department first.");
            return;
        }

        String id = (String) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete department \"" + name + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DepartmentStore.delete(id);
            refreshTable();
        }
    }
}