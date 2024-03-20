package webserver.Session;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManger {

    private static final Map<String, User> loginUsers = new HashMap<>();

    public static User getUser(String sid) {
        return loginUsers.get(sid);
    }

    public static boolean isLogin(String sid) {
        return loginUsers.containsKey(sid);
    }

    public static void addLoginUser(String sid, User user) {
        loginUsers.put(sid, user);
    }

}
