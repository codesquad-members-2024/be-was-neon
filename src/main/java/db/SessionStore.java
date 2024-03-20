package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class SessionStore {
    private static final Map<String, User> sessions = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SessionStore.class);
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    public static User getSession(String sessionId){
        return sessions.get(sessionId);
    }

    public static int getSize(){
        return sessions.size();
    }

    public static void addSession(String sessionId, User user , long expirationTimeMills) {
        sessions.put(sessionId, user);
        setExpiration(sessionId, expirationTimeMills);
    }

    public static void removeSession(String sessionId){
        if(sessions.remove(sessionId )!= null) log.info("Removing log-out Session : " + sessionId);
    }

    private static void setExpiration(String sessionId, long expirationTimeMillis) {
        Runnable removeTask = () -> {
            if(sessions.remove(sessionId) !=null) log.info("Removing expired session: " + sessionId);
        };

        executorService.schedule(removeTask, expirationTimeMillis, TimeUnit.MILLISECONDS);
    }
}

