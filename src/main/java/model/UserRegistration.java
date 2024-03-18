package model;

import db.Database;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UserRegistration {

    enum UserInfo {
        ID("id"), NICKNAME("nickname"), PASSWORD("password"), EMAIL("email");

        private String info;

        UserInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    public static boolean registration(Map<String, String> queryString)
        throws UnsupportedEncodingException {
        String userId = queryString.get(UserInfo.ID.getInfo());
        String name = queryString.get(UserInfo.NICKNAME.getInfo());
        String password = queryString.get(UserInfo.PASSWORD.getInfo());
        String email = queryString.get(UserInfo.EMAIL.getInfo());

        //유저 객체 생성
        User user = new User(userId, password, name, email);

        //데이터 베이스에 저장
        Database.addUser(user);
        return true;
    }

}

