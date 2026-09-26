package gui;

import java.util.ArrayList;
import java.util.List;
import persistence.AccountFileException;
import persistence.AccountFileManager;
import persistence.InvalidAccountDataException;

public class UserStore {

    // Relative path — a "data" folder will be created next to
    // wherever the program is run from.
    private static final String FILE_PATH = "data/accounts.txt";
    private static final AccountFileManager fileManager = new AccountFileManager();
    private static final ArrayList<Account> accounts = new ArrayList<>();

    // Loads existing accounts from the .txt file the first time
    // this class is used, so registered users persist across runs.
    static {
        try {
            List<Account> loaded = fileManager.loadAccounts(FILE_PATH);
            accounts.addAll(loaded);
        } catch (AccountFileException | InvalidAccountDataException ex) {
            // First run (file doesn't exist) or corrupted file —
            // start with an empty list rather than crashing the app.
            System.err.println("Could not load accounts: " + ex.getMessage());
        }
    }

    public static void add(Account account) {
        accounts.add(account);
        persist();
    }

    public static void update(String username, Account updatedAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equalsIgnoreCase(username)) {
                accounts.set(i, updatedAccount);
                break;
            }
        }
        persist();
    }

    public static void delete(String username) {
        accounts.removeIf(a -> a.getUsername().equalsIgnoreCase(username));
        persist();
    }

    public static ArrayList<Account> getAll() {
        return accounts;
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
        int max = 0;
        for (Account account : accounts) {
            String id = account.getUser().getUserId();
            if (id != null && id.startsWith(rolePrefix)) {
                try {
                    int num = Integer.parseInt(id.substring(rolePrefix.length()));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) {
                    // id didn't end in a number, skip it
                }
            }
        }
        return rolePrefix + String.format("%03d", max + 1);
    }

    // Writes the full in-memory account list back to the .txt file.
    // Called after every registration so data survives a restart.
    private static void persist() {
        try {
            fileManager.saveAccounts(accounts, FILE_PATH);
        } catch (AccountFileException ex) {
            System.err.println("Could not save accounts: " + ex.getMessage());
        }
    }

}
