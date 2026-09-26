package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminBillingConfigFrame extends JFrame {
    private DefaultTableModel ratesModel;
    private DefaultListModel<String> networksModel;
    private JTable ratesTable;
    private JList<String> networksList;

    public AdminBillingConfigFrame() {
        setTitle("Billing Configuration");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 10, 10));

        add(buildRatesPanel());
        add(buildNetworksPanel());
    }

    private JPanel buildRatesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Consultation Rates"));

        String[] cols = {"Type", "Base Rate ($)"};
        ratesModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (String type : ConfigStore.getRates().keySet()) {
            ratesModel.addRow(new Object[]{type, ConfigStore.getRates().get(type)});
        }
        ratesTable = new JTable(ratesModel);
        panel.add(new JScrollPane(ratesTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton updateBtn = new JButton("Update Rate");
        updateBtn.addActionListener(e -> {
            int row = ratesTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a rate to update.");
                return;
            }
            String type = (String) ratesModel.getValueAt(row, 0);
            String input = JOptionPane.showInputDialog(this, "Enter new rate for " + type + ":");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double newRate = Double.parseDouble(input.trim());
                    if (newRate < 0) throw new NumberFormatException();
                    ConfigStore.updateRate(type, newRate);
                    ratesModel.setValueAt(newRate, row, 1);
                    JOptionPane.showMessageDialog(this, "Rate updated successfully.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount.");
                }
            }
        });
        btnPanel.add(updateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildNetworksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Insurance Networks"));

        networksModel = new DefaultListModel<>();
        for (String net : ConfigStore.getNetworks()) {
            networksModel.addElement(net);
        }
        networksList = new JList<>(networksModel);
        panel.add(new JScrollPane(networksList), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");

        addBtn.addActionListener(e -> {
            String net = JOptionPane.showInputDialog(this, "Enter Insurance Network Name:");
            if (net != null && !net.trim().isEmpty()) {
                ConfigStore.addNetwork(net.trim());
                networksModel.addElement(net.trim());
            }
        });

        removeBtn.addActionListener(e -> {
            String selected = networksList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a network to remove.");
                return;
            }
            ConfigStore.removeNetwork(selected);
            networksModel.removeElement(selected);
        });

        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}
