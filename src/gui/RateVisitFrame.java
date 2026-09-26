package gui;

import models.Doctor;
import models.Rating;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RateVisitFrame extends JFrame {

    private final User patient;
    private JTextField doctorIdField;
    private JComboBox<Integer> scoreDropdown;
    private JTextArea commentArea;

    public RateVisitFrame(User patient) {
        this.patient = patient;

        setTitle("Rate Doctor / Visit");
        setSize(380, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Rate Doctor / Visit", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Doctor ID:"), gbc);
        doctorIdField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(doctorIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Rating (1-5):"), gbc);
        scoreDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        scoreDropdown.setSelectedItem(5);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(scoreDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Comment:"), gbc);
        commentArea = new JTextArea(4, 15);
        commentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(commentArea);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(scrollPane, gbc);

        JButton submitButton = new JButton("Submit Rating");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(submitButton, gbc);
        submitButton.addActionListener(e -> handleSubmit());

        add(panel);
    }

    private void handleSubmit() {
        String doctorId = doctorIdField.getText().trim();
        int score = (Integer) scoreDropdown.getSelectedItem();
        // Commas/newlines would break our simple .txt line format, so
        // sanitize free-text input before saving.
        String comment = commentArea.getText().trim().replace(",", ";").replace("\n", " ");

        if (doctorId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doctor ID is required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean doctorExists = false;
        for (Account account : UserStore.getAll()) {
            if (account.getUser().getUserId().equalsIgnoreCase(doctorId)
                    && account.getUser() instanceof Doctor) {
                doctorExists = true;
                break;
            }
        }
        if (!doctorExists) {
            JOptionPane.showMessageDialog(this,
                    "No doctor found with ID \"" + doctorId + "\".",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = RatingStore.generateNextId();
        String date = LocalDate.now().toString();
        Rating rating = new Rating(id, patient.getUserId(), doctorId, score, comment, date);
        RatingStore.add(rating);

        JOptionPane.showMessageDialog(this, "Thank you! Your rating has been submitted.");
        dispose();
    }
}