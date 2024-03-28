package session;

import db.UserDatabase;
import http.RequestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import model.User;

public class SessionManager {
    private final RequestManager requestManager;

    public SessionManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public boolean isAuthorizedUser() {
        Optional<String> optCookie = requestManager.getCookie();
        if (optCookie.isEmpty()) {
            return false;
        }
        String sid = requestManager.getSid();
        if (isSessionIdExists(sid)) {
            return true;
        }
        return false;
    }

    public String getUserName() {
        String sid = requestManager.getSid();
        String userId = SessionMemory.findUserIdBySid(sid);
        User user = UserDatabase.findUserById(userId);
        return user.getName();
    }

    public List<User> getUsers() {
        Collection<String> allUserId = SessionMemory.findAllUserId();
        List<User> users = new ArrayList<>();
        allUserId.forEach(userID -> users.add(UserDatabase.findUserById(userID)));

        return users;
    }

    public void expireSession() {
        String sid = requestManager.getSid();
        SessionMemory.removeSession(sid);
    }

    public static void setSession(String sid, String userId) {
        SessionMemory.addSession(sid, userId);
    }

    public static boolean isSessionIdExists(String sid) {
        return SessionMemory.findSessionId(sid);
    }
}
