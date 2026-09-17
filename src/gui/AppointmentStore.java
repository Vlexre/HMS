package gui;

import models.Appointment;
import java.util.ArrayList;

// TEMPORARY in-memory storage. Replace with real .txt file
// read/write once Kaung's file I/O utilities are ready.
public class AppointmentStore {

    private static final ArrayList<Appointment> appointments = new ArrayList<>();
    private static int nextId = 1;

    public static void add(Appointment appointment) {
        appointments.add(appointment);
    }

    public static ArrayList<Appointment> getAll() {
        return appointments;
    }

    public static String generateNextId() {
        return "APT" + String.format("%03d", nextId++);
    }
}