package gui;

import java.io.*;
import java.util.*;

public class ConfigStore {
    private static final String RATES_FILE = "data/consultation_rates.txt";
    private static final String NETWORKS_FILE = "data/insurance_networks.txt";

    private static Map<String, Double> rates = new LinkedHashMap<>();
    private static List<String> networks = new ArrayList<>();

    static {
        loadRates();
        loadNetworks();
    }

    public static Map<String, Double> getRates() {
        return rates;
    }

    public static List<String> getNetworks() {
        return networks;
    }

    public static void updateRate(String type, double rate) {
        rates.put(type, rate);
        saveRates();
    }

    public static void addNetwork(String network) {
        if (!networks.contains(network)) {
            networks.add(network);
            saveNetworks();
        }
    }

    public static void removeNetwork(String network) {
        networks.remove(network);
        saveNetworks();
    }

    private static void loadRates() {
        rates.clear();
        File file = new File(RATES_FILE);
        if (!file.exists()) {
            rates.put("Standard Consultation", 50.0);
            rates.put("Specialist Consultation", 100.0);
            rates.put("Follow-up Visit", 30.0);
            rates.put("Emergency Visit", 150.0);
            rates.put("Surgery", 500.0);
            saveRates();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    rates.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveRates() {
        try {
            new File("data").mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(RATES_FILE))) {
                for (Map.Entry<String, Double> entry : rates.entrySet()) {
                    bw.write(entry.getKey() + "," + entry.getValue());
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadNetworks() {
        networks.clear();
        File file = new File(NETWORKS_FILE);
        if (!file.exists()) {
            networks.add("BUPA");
            networks.add("Allianz");
            networks.add("Cigna");
            saveNetworks();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    networks.add(line.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveNetworks() {
        try {
            new File("data").mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(NETWORKS_FILE))) {
                for (String net : networks) {
                    bw.write(net);
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
