package models;

public class ConsultationNote {

    private String noteId;
    private String patientId;
    private String doctorId;
    private String date;
    private String bloodPressure;
    private String heartRate;
    private String temperature;
    private String notes;

    public ConsultationNote(String noteId, String patientId, String doctorId, String date,
                             String bloodPressure, String heartRate, String temperature, String notes) {
        this.noteId = noteId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.notes = notes;
    }

    public String getNoteId() { return noteId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getBloodPressure() { return bloodPressure; }
    public String getHeartRate() { return heartRate; }
    public String getTemperature() { return temperature; }
    public String getNotes() { return notes; }
}