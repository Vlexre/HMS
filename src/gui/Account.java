package gui;

import models.User;

// Wraps login credentials together with the domain User object.
// TEMPORARY: replace with real persisted accounts once Kaung's
// file I/O is ready.
public class Account {

    private final String username;
    private final String password;
    private final User user;

    public Account(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }
}