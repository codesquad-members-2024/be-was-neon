package db;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionStore {
    private static Map<String, User> sessions = new HashMap<>();

    public static boolean sessionIdExists(String sid) {
        return sessions.keySet().stream()
                .anyMatch(sessionId -> sessionId.equals(sid));
    }

    public static void expireSid(String sid) {
        sessions.remove(sid);
    }

    public static void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    public static User getUserBySid(String sid) {
        return sessions.get(sid);
    }
}
