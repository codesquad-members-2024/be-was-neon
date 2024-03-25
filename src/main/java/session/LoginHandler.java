package session;

import db.Database;
import model.User;
import userservice.UserHandler;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class LoginHandler {
    private static User user;
    public static boolean isLoginDataValid(String queryParam) throws UnsupportedEncodingException {
        Map<String,String> loginData = UserHandler.extractUserData(queryParam);

        if (Database.findUserById(loginData.get("userId")) != null){
            user = Database.findUserById(loginData.get("userId"));
            if (user.getPassword().equals(loginData.get("password"))){
                return true;
            }
        }
        return false;
    }

    public static User getLoginUser(){
        return user;
    }
}
