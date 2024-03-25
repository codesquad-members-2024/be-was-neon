package userservice;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.net.URLDecoder.decode;

public class UserHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    public static User createUserFromData(Map<String, String> paramMap){
        return new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("nickname"));
    }

    public static void addUserToDataBase(User user){
        Database.addUser(user);
    }

    public static Map<String, String> extractUserData(String userData) throws UnsupportedEncodingException {
        String[] params = userData.split("&");
        Map<String, String> paramMap = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("="); // "="를 기준으로 파라미터를 키-값으로 나눕니다.
            paramMap.put(keyValue[0],decode(keyValue[1],"UTF-8"));
        }
        return paramMap;
    }
}
