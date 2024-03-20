package response;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    public static User createUserFromData(Map<String, String> paramMap){
        return new User(paramMap.get("username"), paramMap.get("password"), paramMap.get("nickname"));
    }
    public static void addUserToDataBase(User user){
        logger.info("New User: {}", user);
        Database.addUser(user);
        logger.info("Every User: {}", Database.findAll());
    }
}
