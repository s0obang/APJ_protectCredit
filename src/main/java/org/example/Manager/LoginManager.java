package org.example.Manager;

import org.example.entity.User;

public class LoginManager {
    private static DatabaseManager dbManager;
    private static User loggedInUser;

    public LoginManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        loggedInUser = null; // 처음 시작했을 때
    }
    public static boolean saveLoggedInUser(User user) {
        User verifiedUser = dbManager.findUser(user);
        if (verifiedUser != null) {
            loggedInUser = verifiedUser;
            return true;
        }
        return false;
    }
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public void logout() {
        loggedInUser = null;
    }

}
