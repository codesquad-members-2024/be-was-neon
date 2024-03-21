package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SessionStore {
    private static Map<String, User> sessions = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SessionStore.class);

    public static User getSession(String sessionId){
        return sessions.get(sessionId);
    }

    public static int getSize(){
        return sessions.size();
    }

    public static void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
        log.debug("New Session : " + sessionId);
    }

    public static void removeSession(String sessionId){
        if(sessions.remove(sessionId )!= null) log.info("Removing log-out Session : " + sessionId);
    }

    public static void clear(){
        sessions = new HashMap<>();
    }

}

