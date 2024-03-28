package webserver;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, User> sessions = new HashMap<>();

    public static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void saveSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static Optional<User> findUser(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
