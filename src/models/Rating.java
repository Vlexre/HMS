package models;

public class Rating {

    private String ratingId;
    private String patientId;
    private String doctorId;
    private int score;
    private String comment;
    private String date;

    public Rating(String ratingId, String patientId, String doctorId,
                  int score, String comment, String date) {
        this.ratingId = ratingId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }

    public String getRatingId() { return ratingId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public int getScore() { return score; }
    public String getComment() { return comment; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return ratingId + " - " + score + "/5";
    }
}