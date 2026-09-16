import models.*;
import utils.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList<>();

        users.add(new Patient("P001", "Zara", "zara@email.com", "0121111111",
                "2002-05-10", "MRN001"));
        users.add(new Doctor("D001", "Adam", "adam@email.com", "0122222222",
                "Cardiology", "LIC001"));
        users.add(new AdminStaff("A001", "Maya", "maya@email.com", "0123333333"));
        users.add(new MedicalManager("M001", "John", "john@email.com", "0124444444",
                "Emergency"));

        System.out.println("=== POLYMORPHISM ===");
        for (User user : users) {
            System.out.println(user.getProfileSummary());
        }

        UserOperations.sortByName(users);

        System.out.println("\n=== USERS SORTED BY NAME ===");
        for (User user : users) {
            System.out.println(user.getName());
        }

        User foundUser = UserOperations.searchById(users, "D001");

        System.out.println("\n=== USER SEARCH ===");
        System.out.println(foundUser != null
                ? foundUser.getProfileSummary()
                : "User not found");

        ArrayList<Appointment> appointments = new ArrayList<>();

        appointments.add(new Appointment(
                "AP003", "P001", "D001",
                "2026-09-20", "14:00", "Booked"));

        appointments.add(new Appointment(
                "AP001", "P001", "D001",
                "2026-09-18", "10:00", "Booked"));

        appointments.add(new Appointment(
                "AP002", "P001", "D001",
                "2026-09-19", "12:00", "Booked"));

        AppointmentOperations.sortByDateTime(appointments);

        System.out.println("\n=== APPOINTMENTS SORTED BY DATE/TIME ===");
        for (Appointment appointment : appointments) {
            System.out.println(
                    appointment.getAppointmentId() + " - "
                    + appointment.getDate() + " "
                    + appointment.getTime()
            );
        }

        Appointment foundAppointment =
                AppointmentOperations.searchById(appointments, "AP002");

        System.out.println("\n=== APPOINTMENT SEARCH ===");
        System.out.println(foundAppointment != null
                ? foundAppointment.getAppointmentId() + " found"
                : "Appointment not found");
    }
}
