package session;

import model.User;
import userservice.LoginHandler;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final int SESSION_ID_LENGTH = 6;
    private static final Map<String, User> sessions = new ConcurrentHashMap<>();

    public static String createSession(User user){
        String sessionId;
        do {
            sessionId = generateSessionId();
        } while (!checkIsSessionIDExist(sessionId));
        sessions.put(sessionId, user);
        return sessionId;
    }

    public static User getUserBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static String generateSessionId() {
        StringBuilder sb = new StringBuilder(SESSION_ID_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    public static Boolean checkIsSessionIDExist(String sessionId){
        return !sessions.containsKey(sessionId);
    }
}
