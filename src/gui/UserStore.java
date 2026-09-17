package gui;

import java.util.ArrayList;

// TEMPORARY in-memory storage. Replace with real .txt file
// read/write once Kaung's file I/O utilities are ready.
public class UserStore {

    private static final ArrayList<Account> accounts = new ArrayList<>();
    private static int nextId = 1;

    public static void add(Account account) {
        accounts.add(account);
    }

    public static Account findByUsername(String username, String password) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)
                    && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static String generateNextUserId(String rolePrefix) {
        return rolePrefix + String.format("%03d", nextId++);
    }
}