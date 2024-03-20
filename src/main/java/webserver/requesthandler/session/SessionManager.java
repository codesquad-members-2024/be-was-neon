package webserver.requesthandler.session;

import java.net.HttpCookie;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;

public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "SID";
    public static final int EXPIRATION_PERIOD = 1800;
    private static final Map<String, Object> sessions = new ConcurrentHashMap<>();

    public static void createSession(Object object, HttpResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, object);

        HttpCookie cookie = new HttpCookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setMaxAge(EXPIRATION_PERIOD); // 만료 시간 30분
        response.addCookie(cookie);
    }

    public static void createSessionBySID(Object object, HttpResponse response, String sid) {
        sessions.put(sid, object);

        HttpCookie cookie = new HttpCookie(SESSION_COOKIE_NAME, sid);
        response.addCookie(cookie);
    }

    public static Object findSession(HttpRequest request) {
        HttpCookie cookie = request.getCookie(SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        String sessionId = cookie.getValue();
        return sessions.get(sessionId);
    }

    public static void expire(HttpRequest request) {
        HttpCookie cookie = request.getCookie(SESSION_COOKIE_NAME);
        if (cookie != null) {
            sessions.remove(cookie.getValue());
        }
    }
}
