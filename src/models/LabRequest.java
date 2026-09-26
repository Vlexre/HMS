package models;

public class LabRequest {

    private String requestId;
    private String patientId;
    private String doctorId;
    private String testType;
    private String status;
    private String dateRequested;

    public LabRequest(String requestId, String patientId, String doctorId,
                       String testType, String status, String dateRequested) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testType = testType;
        this.status = status;
        this.dateRequested = dateRequested;
    }

    public String getRequestId() { return requestId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getTestType() { return testType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDateRequested() { return dateRequested; }
}