package model;

import db.Database;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UserRegistration {

    public static void registration(Map<String,String> queryString) throws UnsupportedEncodingException {

        //유저 객체 생성
        User user =
            new User(queryString.get("id"), queryString.get("nickname"), queryString.get("password"), queryString.get("email"));

        //데이터 베이스에 저장
        Database.addUser(user);
    }

}

