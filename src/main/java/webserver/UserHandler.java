package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    public static void createUserFromData(Map<String, String> paramMap){
        User user = new User(paramMap.get("username"), paramMap.get("password"), paramMap.get("nickname"));
        logger.debug("New User Made: {}", user);
        Database.addUser(user);
        logger.debug("Every User: {}", Database.findAll());
    }
}
