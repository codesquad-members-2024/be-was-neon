package model;

import db.Database;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UserRegistration {

    //문자열 상수로 변경
    private static final String USER_ID ="id";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";

    public static boolean register(Map<String, String> queryString)
        throws UnsupportedEncodingException {
        String userId = queryString.get(USER_ID);
        String name = queryString.get(NICKNAME);
        String password = queryString.get(PASSWORD);
        String email = queryString.get(EMAIL);

        //유저 객체 생성
        User user = new User(userId, password, name, email);

        //데이터 베이스에 저장
        Database.addUser(user);
        return true;
    }

}

