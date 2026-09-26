package models;

public class Appointment implements Comparable<Appointment> {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    private String status;

    public Appointment(String appointmentId, String patientId, String doctorId,
            String date, String time, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(Appointment other) {
        return (date + time).compareTo(other.date + other.time);
    }
}
