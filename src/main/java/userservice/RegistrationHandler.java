package userservice;

import db.Database;
import model.User;

import java.util.Map;

public class RegistrationHandler {
    public void registerNewUser(Map<String, String> paramMap){
        addUserToDataBase(createUserFromData(paramMap));
    }
    private User createUserFromData(Map<String, String> paramMap){
        return new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("nickname"));
    }

    private void addUserToDataBase(User user){
        Database.addUser(user);
    }

}
