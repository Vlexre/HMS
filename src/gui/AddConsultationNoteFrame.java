package gui;

import models.ConsultationNote;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddConsultationNoteFrame extends JFrame {

    private final User doctor;
    private final String patientId;
    private final Runnable onSaved;

    private JTextField bpField;
    private JTextField hrField;
    private JTextField tempField;
    private JTextArea notesArea;

    public AddConsultationNoteFrame(User doctor, String patientId, Runnable onSaved) {
        this.doctor = doctor;
        this.patientId = patientId;
        this.onSaved = onSaved;

        setTitle("Add Consultation Note - Patient " + patientId);
        setSize(380, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        JLabel title = new JLabel("New Consultation Note", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Blood Pressure:"), gbc);
        bpField = new JTextField("120/80", 12);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(bpField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Heart Rate (bpm):"), gbc);
        hrField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(hrField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Temperature (°C):"), gbc);
        tempField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(tempField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Notes:"), gbc);
        notesArea = new JTextArea(4, 12);
        notesArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(notesArea);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(scrollPane, gbc);
        row++;

        JButton saveButton = new JButton("Save Note");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(saveButton, gbc);
        saveButton.addActionListener(e -> handleSave());

        add(panel);
    }

    private void handleSave() {
        String bp = bpField.getText().trim();
        String hr = hrField.getText().trim();
        String temp = tempField.getText().trim();
        String noteText = notesArea.getText().trim().replace(",", ";").replace("\n", " ");

        if (bp.isEmpty() || hr.isEmpty() || temp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Blood pressure, heart rate, and temperature are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!hr.matches("\\d{2,3}")) {
            JOptionPane.showMessageDialog(this,
                    "Heart rate must be a number (e.g. 72).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = ConsultationNoteStore.generateNextId();
        String date = LocalDate.now().toString();
        ConsultationNote note = new ConsultationNote(
                id, patientId, doctor.getUserId(), date, bp, hr, temp, noteText);

        ConsultationNoteStore.add(note);

        JOptionPane.showMessageDialog(this, "Consultation note saved.");
        onSaved.run();
        dispose();
    }
}