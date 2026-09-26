package persistence;

import models.Prescription;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionFileManager {

    // Format per line: prescriptionId,patientId,doctorId,medication,dosage,instructions,dateIssued
    public void savePrescriptions(List<Prescription> prescriptions, String filePath)
            throws PrescriptionFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (Prescription p : prescriptions) {
                String line = p.getPrescriptionId() + "," + p.getPatientId() + "," + p.getDoctorId() + ","
                        + p.getMedication() + "," + p.getDosage() + "," + p.getInstructions() + ","
                        + p.getDateIssued();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PrescriptionFileException("Could not save prescriptions to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<Prescription> loadPrescriptions(String filePath)
            throws PrescriptionFileException, InvalidPrescriptionDataException {
        List<Prescription> prescriptions = new ArrayList<Prescription>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 7) {
                        throw new InvalidPrescriptionDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String prescriptionId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    String medication = parts[3].trim();
                    String dosage = parts[4].trim();
                    String instructions = parts[5].trim();
                    String dateIssued = parts[6].trim();

                    if (prescriptionId.equals("") || patientId.equals("") || doctorId.equals("")) {
                        throw new InvalidPrescriptionDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    prescriptions.add(new Prescription(prescriptionId, patientId, doctorId,
                            medication, dosage, instructions, dateIssued));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return prescriptions;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return prescriptions;
    }
}