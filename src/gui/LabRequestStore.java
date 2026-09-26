package gui;

import java.util.ArrayList;
import java.util.List;
import models.LabRequest;
import persistence.InvalidLabRequestDataException;
import persistence.LabRequestFileException;
import persistence.LabRequestFileManager;

public class LabRequestStore {

    private static final String FILE_PATH = "data/lab_requests.txt";
    private static final LabRequestFileManager fileManager = new LabRequestFileManager();
    private static final ArrayList<LabRequest> requests = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<LabRequest> loaded = fileManager.loadRequests(FILE_PATH);
            requests.addAll(loaded);
            for (LabRequest r : requests) {
                String id = r.getRequestId();
                if (id != null && id.startsWith("LR")) {
                    try {
                        int num = Integer.parseInt(id.substring(2));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (LabRequestFileException | InvalidLabRequestDataException ex) {
            System.err.println("Could not load lab requests: " + ex.getMessage());
        }
    }

    public static void add(LabRequest request) {
        requests.add(request);
        persist();
    }

    public static void updateStatus(String requestId, String status) {
        for (LabRequest r : requests) {
            if (r.getRequestId().equalsIgnoreCase(requestId)) {
                r.setStatus(status);
                break;
            }
        }
        persist();
    }

    public static ArrayList<LabRequest> getAll() {
        return requests;
    }

    public static String generateNextId() {
        return "LR" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.saveRequests(requests, FILE_PATH);
        } catch (LabRequestFileException ex) {
            System.err.println("Could not save lab requests: " + ex.getMessage());
        }
    }
}