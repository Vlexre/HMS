package gui;

import java.util.ArrayList;
import java.util.List;
import models.Rating;
import persistence.InvalidRatingDataException;
import persistence.RatingFileException;
import persistence.RatingFileManager;

public class RatingStore {

    private static final String FILE_PATH = "data/ratings.txt";
    private static final RatingFileManager fileManager = new RatingFileManager();
    private static final ArrayList<Rating> ratings = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<Rating> loaded = fileManager.loadRatings(FILE_PATH);
            ratings.addAll(loaded);
            for (Rating r : ratings) {
                String id = r.getRatingId();
                if (id != null && id.startsWith("RT")) {
                    try {
                        int num = Integer.parseInt(id.substring(2));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (RatingFileException | InvalidRatingDataException ex) {
            System.err.println("Could not load ratings: " + ex.getMessage());
        }
    }

    public static void add(Rating rating) {
        ratings.add(rating);
        persist();
    }

    public static ArrayList<Rating> getAll() {
        return ratings;
    }

    public static String generateNextId() {
        return "RT" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.saveRatings(ratings, FILE_PATH);
        } catch (RatingFileException ex) {
            System.err.println("Could not save ratings: " + ex.getMessage());
        }
    }
}