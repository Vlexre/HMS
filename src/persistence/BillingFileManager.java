package persistence;

import models.Billing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BillingFileManager {

    // Format per line: billingId,patientId,doctorId,serviceType,amount,insuranceCovered,status,dateIssued
    public void saveBillings(List<Billing> billings, String filePath) throws BillingFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (Billing b : billings) {
                String line = b.getBillingId() + "," + b.getPatientId() + "," + b.getDoctorId() + ","
                        + b.getServiceType() + "," + b.getAmount() + "," + b.isInsuranceCovered() + ","
                        + b.getStatus() + "," + b.getDateIssued() + "," + b.getInsuranceNetwork();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new BillingFileException("Could not save billing records to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<Billing> loadBillings(String filePath)
            throws BillingFileException, InvalidBillingDataException {
        List<Billing> billings = new ArrayList<Billing>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 8) {
                        throw new InvalidBillingDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String billingId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    String serviceType = parts[3].trim();
                    String amountStr = parts[4].trim();
                    String insuranceStr = parts[5].trim();
                    String status = parts[6].trim();
                    String dateIssued = parts[7].trim();
                    String insuranceNetwork = parts.length > 8 ? parts[8].trim() : "None";

                    if (billingId.equals("") || patientId.equals("")) {
                        throw new InvalidBillingDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(amountStr);
                    } catch (NumberFormatException nfe) {
                        throw new InvalidBillingDataException(
                                "Line " + lineNumber + " has an invalid amount: " + line);
                    }

                    boolean insuranceCovered = Boolean.parseBoolean(insuranceStr);

                    billings.add(new Billing(billingId, patientId, doctorId, serviceType,
                            amount, insuranceCovered, insuranceNetwork, status, dateIssued));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return billings;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return billings;
    }
}