package utils;

import models.User;
import java.util.ArrayList;
import java.util.Comparator;

public class UserOperations {

    public static void sortByName(ArrayList<User> users) {
        users.sort(Comparator.comparing(User::getName));
    }

    public static User searchById(ArrayList<User> users, String userId) {
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                return user;
            }
        }
        return null;
    }
}
