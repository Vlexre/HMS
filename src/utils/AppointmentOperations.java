package utils;

import models.Appointment;
import java.util.ArrayList;
import java.util.Collections;

public class AppointmentOperations {

    public static void sortByDateTime(ArrayList<Appointment> appointments) {
        Collections.sort(appointments);
    }

    public static Appointment searchById(ArrayList<Appointment> appointments, String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }
}
