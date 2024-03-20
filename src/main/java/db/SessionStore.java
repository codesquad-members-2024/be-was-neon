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

    public static User getSession(String cookie){
        return sessions.get(cookie);
    }

    public static int getSize(){
        return sessions.size();
    }

    public static void addSession(String cookie, User user , long expirationTimeMills) {
        sessions.put(cookie, user);
        setExpiration(cookie, expirationTimeMills);
    }

    public static void removeSession(String cookie){
        if(sessions.remove(cookie )!= null) log.info("Removing log-out Cookie : " + cookie);
    }

    private static void setExpiration(String cookie, long expirationTimeMillis) {
        Runnable removeTask = () -> {
            if(sessions.remove(cookie) !=null) log.info("Removing expired session: " + cookie);
        };

        executorService.schedule(removeTask, expirationTimeMillis, TimeUnit.MILLISECONDS);
    }
}

