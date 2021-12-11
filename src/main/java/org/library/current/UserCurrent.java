package org.library.current;

import org.library.model.User;

public class UserCurrent {
    private User user;
    private static UserCurrent userCurrent;

    static {
        userCurrent = new UserCurrent();
    }

    private UserCurrent() {

    }

    public static void add(User user) {
        userCurrent.setUser(user);
    }

    private void setUser(User user) {
        userCurrent.user = user;
    }
    public static UserCurrent getUserCurrent() {
        return userCurrent;
    }

    public User getUser() {
        return userCurrent.user;
    }
}
