package gui;

import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageUsersFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public ManageUsersFrame() {
        setTitle("Manage Users");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Username", "User ID", "Name", "Role", "Email", "Phone"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Account account : UserStore.getAll()) {
            User user = account.getUser();
            model.addRow(new Object[]{
                    account.getUsername(),
                    user.getUserId(),
                    user.getName(),
                    user.getRole(),
                    user.getEmail(),
                    user.getPhoneNumber()
            });
        }
    }

    private Account getSelectedAccount() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.");
            return null;
        }
        String username = (String) model.getValueAt(row, 0);
        for (Account account : UserStore.getAll()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    private void handleEdit() {
        Account selected = getSelectedAccount();
        if (selected == null) return;

        EditUserFrame editFrame = new EditUserFrame(selected, this::refreshTable);
        editFrame.setVisible(true);
    }

    private void handleDelete() {
        Account selected = getSelectedAccount();
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user \"" + selected.getUser().getName() + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            UserStore.delete(selected.getUsername());
            refreshTable();
        }
    }
}