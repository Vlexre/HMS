package persistence;

import models.ConsultationNote;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationNoteFileManager {

    // Format per line: noteId,patientId,doctorId,date,bloodPressure,heartRate,temperature,notes
    public void saveNotes(List<ConsultationNote> notes, String filePath)
            throws ConsultationNoteFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (ConsultationNote n : notes) {
                String line = n.getNoteId() + "," + n.getPatientId() + "," + n.getDoctorId() + ","
                        + n.getDate() + "," + n.getBloodPressure() + "," + n.getHeartRate() + ","
                        + n.getTemperature() + "," + n.getNotes();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ConsultationNoteFileException("Could not save consultation notes to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<ConsultationNote> loadNotes(String filePath)
            throws ConsultationNoteFileException, InvalidConsultationNoteDataException {
        List<ConsultationNote> notes = new ArrayList<ConsultationNote>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 8) {
                        throw new InvalidConsultationNoteDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String noteId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    String date = parts[3].trim();
                    String bp = parts[4].trim();
                    String hr = parts[5].trim();
                    String temp = parts[6].trim();
                    String noteText = parts[7].trim();

                    if (noteId.equals("") || patientId.equals("") || doctorId.equals("")) {
                        throw new InvalidConsultationNoteDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    notes.add(new ConsultationNote(noteId, patientId, doctorId, date, bp, hr, temp, noteText));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return notes;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return notes;
    }
}