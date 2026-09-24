package persistence;

import models.Appointment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentFileManager {

    // Format per line: appointmentId,patientId,doctorId,date,time,status
    public void saveAppointments(List<Appointment> appointments, String filePath)
            throws AppointmentFileException {
        BufferedWriter writer = null;

        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            writer = new BufferedWriter(new FileWriter(filePath));

            for (Appointment appointment : appointments) {
                String line = appointment.getAppointmentId() + "," + appointment.getPatientId() + ","
                        + appointment.getDoctorId() + "," + appointment.getDate() + ","
                        + appointment.getTime() + "," + appointment.getStatus();
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new AppointmentFileException("Could not save appointments to " + filePath);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore close error
                }
            }
        }
    }

    public List<Appointment> loadAppointments(String filePath)
            throws AppointmentFileException, InvalidAppointmentDataException {
        List<Appointment> appointments = new ArrayList<Appointment>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;

            while (line != null) {
                lineNumber++;

                if (!line.trim().equals("")) {
                    String[] parts = line.split(",");

                    if (parts.length < 6) {
                        throw new InvalidAppointmentDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }

                    String appointmentId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    String date = parts[3].trim();
                    String time = parts[4].trim();
                    String status = parts[5].trim();

                    if (appointmentId.equals("") || patientId.equals("") || doctorId.equals("")) {
                        throw new InvalidAppointmentDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    appointments.add(new Appointment(appointmentId, patientId, doctorId, date, time, status));
                }

                line = reader.readLine();
            }

        } catch (IOException e) {
            // file might not exist yet on first run, just return empty list
            return appointments;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore close error
                }
            }
        }

        return appointments;
    }
}