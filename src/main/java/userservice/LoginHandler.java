package userservice;

import db.Database;
import model.User;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class LoginHandler {
    private User user;
    public boolean isLoginDataValid(Map<String,String> loginData) {
        if (Database.findUserById(loginData.get("userId")) != null){
            user = Database.findUserById(loginData.get("userId"));
            if (user.getPassword().equals(loginData.get("password"))){
                return true;
            }
        }
        return false;
    }

    public User getLoginUser(){
        return user;
    }
}
