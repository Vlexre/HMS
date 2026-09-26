package gui;

import java.util.ArrayList;
import java.util.List;
import models.ConsultationNote;
import persistence.ConsultationNoteFileException;
import persistence.ConsultationNoteFileManager;
import persistence.InvalidConsultationNoteDataException;

public class ConsultationNoteStore {

    private static final String FILE_PATH = "data/consultation_notes.txt";
    private static final ConsultationNoteFileManager fileManager = new ConsultationNoteFileManager();
    private static final ArrayList<ConsultationNote> notes = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<ConsultationNote> loaded = fileManager.loadNotes(FILE_PATH);
            notes.addAll(loaded);
            for (ConsultationNote n : notes) {
                String id = n.getNoteId();
                if (id != null && id.startsWith("CN")) {
                    try {
                        int num = Integer.parseInt(id.substring(2));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (ConsultationNoteFileException | InvalidConsultationNoteDataException ex) {
            System.err.println("Could not load consultation notes: " + ex.getMessage());
        }
    }

    public static void add(ConsultationNote note) {
        notes.add(note);
        persist();
    }

    public static ArrayList<ConsultationNote> getAll() {
        return notes;
    }

    public static String generateNextId() {
        return "CN" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.saveNotes(notes, FILE_PATH);
        } catch (ConsultationNoteFileException ex) {
            System.err.println("Could not save consultation notes: " + ex.getMessage());
        }
    }
}