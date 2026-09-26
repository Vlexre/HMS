package persistence;

import models.LabRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LabRequestFileManager {

    // Format per line: requestId,patientId,doctorId,testType,status,dateRequested
    public void saveRequests(List<LabRequest> requests, String filePath)
            throws LabRequestFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (LabRequest r : requests) {
                String line = r.getRequestId() + "," + r.getPatientId() + "," + r.getDoctorId() + ","
                        + r.getTestType() + "," + r.getStatus() + "," + r.getDateRequested();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new LabRequestFileException("Could not save lab requests to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<LabRequest> loadRequests(String filePath)
            throws LabRequestFileException, InvalidLabRequestDataException {
        List<LabRequest> requests = new ArrayList<LabRequest>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                if (!line.trim().equals("")) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 6) {
                        throw new InvalidLabRequestDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String requestId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    String testType = parts[3].trim();
                    String status = parts[4].trim();
                    String dateRequested = parts[5].trim();

                    if (requestId.equals("") || patientId.equals("") || doctorId.equals("")) {
                        throw new InvalidLabRequestDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    requests.add(new LabRequest(requestId, patientId, doctorId, testType, status, dateRequested));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return requests;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return requests;
    }
}