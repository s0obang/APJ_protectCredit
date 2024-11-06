package org.example.Manager;

import org.example.entity.User;

public class LoginManager {
    private DatabaseManager dbManager;
    private User loggedInUser;

    public LoginManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    public boolean saveLoggedInUser(User user) {
        User verifiedUser = dbManager.findUser(user);
        if (verifiedUser != null) {
            loggedInUser = verifiedUser;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedInUser = null;
    }

}
