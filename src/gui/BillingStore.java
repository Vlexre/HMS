package gui;

import java.util.ArrayList;
import java.util.List;
import models.Billing;
import persistence.BillingFileException;
import persistence.BillingFileManager;
import persistence.InvalidBillingDataException;

public class BillingStore {

    private static final String FILE_PATH = "data/billing.txt";
    private static final BillingFileManager fileManager = new BillingFileManager();
    private static final ArrayList<Billing> billings = new ArrayList<>();
    private static int nextId = 1;

    static {
        try {
            List<Billing> loaded = fileManager.loadBillings(FILE_PATH);
            billings.addAll(loaded);
            for (Billing b : billings) {
                String id = b.getBillingId();
                if (id != null && id.startsWith("BILL")) {
                    try {
                        int num = Integer.parseInt(id.substring(4));
                        if (num >= nextId) { nextId = num + 1; }
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (BillingFileException | InvalidBillingDataException ex) {
            System.err.println("Could not load billing records: " + ex.getMessage());
        }
    }

    public static void add(Billing billing) {
        billings.add(billing);
        persist();
    }

    public static void markPaid(String billingId) {
        for (Billing b : billings) {
            if (b.getBillingId().equalsIgnoreCase(billingId)) {
                b.setStatus("Paid");
                break;
            }
        }
        persist();
    }

    public static ArrayList<Billing> getAll() {
        return billings;
    }

    public static String generateNextId() {
        return "BILL" + String.format("%03d", nextId++);
    }

    private static void persist() {
        try {
            fileManager.saveBillings(billings, FILE_PATH);
        } catch (BillingFileException ex) {
            System.err.println("Could not save billing records: " + ex.getMessage());
        }
    }
}