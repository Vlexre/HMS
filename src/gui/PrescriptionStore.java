package gui;

import java.util.ArrayList;
import java.util.List;
import models.Prescription;
import persistence.InvalidPrescriptionDataException;
import persistence.PrescriptionFileException;
import persistence.PrescriptionFileManager;

public class PrescriptionStore {

    private static final String FILE_PATH = "data/prescriptions.txt";
    private static final PrescriptionFileManager fileManager = new PrescriptionFileManager();
    private static final ArrayList<Prescription> prescriptions = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<Prescription> loaded = fileManager.loadPrescriptions(FILE_PATH);
            prescriptions.addAll(loaded);
            for (Prescription p : prescriptions) {
                String id = p.getPrescriptionId();
                if (id != null && id.startsWith("RX")) {
                    try {
                        int num = Integer.parseInt(id.substring(2));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (PrescriptionFileException | InvalidPrescriptionDataException ex) {
            System.err.println("Could not load prescriptions: " + ex.getMessage());
        }
    }

    public static void add(Prescription prescription) {
        prescriptions.add(prescription);
        persist();
    }

    public static ArrayList<Prescription> getAll() {
        return prescriptions;
    }

    public static String generateNextId() {
        return "RX" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.savePrescriptions(prescriptions, FILE_PATH);
        } catch (PrescriptionFileException ex) {
            System.err.println("Could not save prescriptions: " + ex.getMessage());
        }
    }
}