package persistence;

import models.Rating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RatingFileManager {

    // Format per line: ratingId,patientId,doctorId,score,comment,date
    public void saveRatings(List<Rating> ratings, String filePath) throws RatingFileException {
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(filePath));
            for (Rating r : ratings) {
                String line = r.getRatingId() + "," + r.getPatientId() + "," + r.getDoctorId() + ","
                        + r.getScore() + "," + r.getComment() + "," + r.getDate();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RatingFileException("Could not save ratings to " + filePath);
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException e) { }
            }
        }
    }

    public List<Rating> loadRatings(String filePath)
            throws RatingFileException, InvalidRatingDataException {
        List<Rating> ratings = new ArrayList<Rating>();
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
                        throw new InvalidRatingDataException(
                                "Line " + lineNumber + " is missing data: " + line);
                    }
                    String ratingId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String doctorId = parts[2].trim();
                    int score;
                    try {
                        score = Integer.parseInt(parts[3].trim());
                    } catch (NumberFormatException nfe) {
                        throw new InvalidRatingDataException(
                                "Line " + lineNumber + " has an invalid score: " + line);
                    }
                    String comment = parts[4].trim();
                    String date = parts[5].trim();

                    if (ratingId.equals("") || patientId.equals("") || doctorId.equals("")) {
                        throw new InvalidRatingDataException(
                                "Line " + lineNumber + " has an empty field: " + line);
                    }

                    ratings.add(new Rating(ratingId, patientId, doctorId, score, comment, date));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return ratings;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { }
            }
        }
        return ratings;
    }
}