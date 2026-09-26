package gui;

import java.util.ArrayList;
import java.util.List;
import models.Appointment;
import persistence.AppointmentFileException;
import persistence.AppointmentFileManager;
import persistence.InvalidAppointmentDataException;

public class AppointmentStore {

    private static final String FILE_PATH = "data/appointments.txt";
    private static final AppointmentFileManager fileManager = new AppointmentFileManager();
    private static final ArrayList<Appointment> appointments = new ArrayList<>();
    private static int nextId = 1;

    // Loads existing appointments from the .txt file the first time
    // this class is used, so booked appointments persist across runs.
    static {
        try {
            List<Appointment> loaded = fileManager.loadAppointments(FILE_PATH);
            appointments.addAll(loaded);
            for (Appointment appointment : appointments) {
                String id = appointment.getAppointmentId();
                if (id != null && id.startsWith("APT")) {
                    try {
                        int num = Integer.parseInt(id.substring(3));
                        if (num >= nextId) {
                            nextId = num + 1;
                        }
                    } catch (NumberFormatException ignored) {
                        // id didn't end in a number, skip it
                    }
                }
            }
        } catch (AppointmentFileException | InvalidAppointmentDataException ex) {
            System.err.println("Could not load appointments: " + ex.getMessage());
        }
    }

    public static void add(Appointment appointment) {
        appointments.add(appointment);
        persist();
    }

    public static ArrayList<Appointment> getAll() {
        return appointments;
    }

    public static String generateNextId() {
        return "APT" + String.format("%03d", nextId++);
    }

    // Used by Admin to confirm/cancel an appointment.
    public static void updateStatus(String appointmentId, String status) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointment.setStatus(status);
                break;
            }
        }
        persist();
    }

    // Used by Patient to reschedule an appointment; puts it back to
    // Pending so Admin/Doctor can re-confirm the new slot.
    public static void updateSchedule(String appointmentId, String newDate, String newTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                appointment.setStatus("Pending");
                break;
            }
        }
        persist();
    }

    private static void persist() {
        try {
            fileManager.saveAppointments(appointments, FILE_PATH);
        } catch (AppointmentFileException ex) {
            System.err.println("Could not save appointments: " + ex.getMessage());
        }
    }
}
