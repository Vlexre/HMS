package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TableUtils {

    public static void enableCellPopup(JTable table, Component parent) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row != -1 && col != -1) {
                        showFullText(parent, table.getColumnName(col),
                                String.valueOf(table.getValueAt(row, col)));
                    }
                }
            }
        });
    }

    private static void showFullText(Component parent, String columnName, String fullText) {
        JTextArea textArea = new JTextArea(fullText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        JOptionPane.showMessageDialog(parent, scrollPane, columnName, JOptionPane.PLAIN_MESSAGE);
    }
}