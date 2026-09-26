package gui;

import models.ConsultationNote;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ConsultationHistoryFrame extends JFrame {

    public ConsultationHistoryFrame(String patientId) {
        setTitle("Consultation History - Patient " + patientId);
        setSize(650, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Note ID", "Date", "Blood Pressure", "Heart Rate", "Temp", "Notes", "Doctor ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (ConsultationNote note : ConsultationNoteStore.getAll()) {
            if (note.getPatientId().equalsIgnoreCase(patientId)) {
                model.addRow(new Object[]{
                        note.getNoteId(), note.getDate(), note.getBloodPressure(),
                        note.getHeartRate(), note.getTemperature(), note.getNotes(), note.getDoctorId()
                });
            }
        }

        JTable table = new JTable(model);
        TableUtils.enableCellPopup(table, this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(scrollPane, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No consultation notes found yet.");
        }
    }
}