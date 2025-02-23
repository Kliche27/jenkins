package utils;

import models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private String sessionKey;

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user, String sessionKey) {
        this.currentUser = user;
        this.sessionKey = sessionKey;
    }

    public void logout() {
        this.currentUser = null;
        this.sessionKey = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}

