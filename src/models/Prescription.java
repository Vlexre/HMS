package models;

public class Prescription {

    private String prescriptionId;
    private String patientId;
    private String doctorId;
    private String medication;
    private String dosage;
    private String instructions;
    private String dateIssued;

    public Prescription(String prescriptionId, String patientId, String doctorId,
                         String medication, String dosage, String instructions, String dateIssued) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.dateIssued = dateIssued;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getMedication() { return medication; }
    public String getDosage() { return dosage; }
    public String getInstructions() { return instructions; }
    public String getDateIssued() { return dateIssued; }

    @Override
    public String toString() {
        return prescriptionId + " - " + medication + " (" + dosage + ")";
    }
}