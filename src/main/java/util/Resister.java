package util;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Map;

public class Resister {
    private static final Logger logger = LoggerFactory.getLogger(Resister.class);
    private static final String USER_ID = "userId";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";

    public void create(String startLine) {
        Map<String, String> userInfo = Parser.getUserInfo(startLine);
        String userId = userInfo.get(USER_ID);
        String password = userInfo.get(PASSWORD);
        String name = userInfo.get(NAME);
        User user = new User(userId, password, name, userId + "@gmail.com");
        Database.addUser(user);
        logger.debug("[User] - {}", Database.findUserById(userId));
    }

}
