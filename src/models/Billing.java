package models;

public class Billing {

    private String billingId;
    private String patientId;
    private String doctorId;
    private String serviceType;   // acts as the "medical grade" / consultation tier
    private double amount;
    private boolean insuranceCovered;
    private String insuranceNetwork;
    private String status;        // "Unpaid" or "Paid"
    private String dateIssued;

    public Billing(String billingId, String patientId, String doctorId, String serviceType,
                   double amount, boolean insuranceCovered, String insuranceNetwork, String status, String dateIssued) {
        this.billingId = billingId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceType = serviceType;
        this.amount = amount;
        this.insuranceCovered = insuranceCovered;
        this.insuranceNetwork = insuranceNetwork;
        this.status = status;
        this.dateIssued = dateIssued;
    }

    public String getBillingId() { return billingId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getServiceType() { return serviceType; }
    public double getAmount() { return amount; }
    public boolean isInsuranceCovered() { return insuranceCovered; }
    public String getInsuranceNetwork() { return insuranceNetwork; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDateIssued() { return dateIssued; }

    public double getAmountDue() {
        return insuranceCovered ? 0.0 : amount;
    }
}