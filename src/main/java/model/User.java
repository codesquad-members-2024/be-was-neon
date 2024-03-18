package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class User {

    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email)
        throws UnsupportedEncodingException {
        this.userId = URLDecoder.decode(userId, "UTF-8");
        this.password = URLDecoder.decode(password, "UTF-8");
        this.name = URLDecoder.decode(name, "UTF-8");
        this.email = URLDecoder.decode(email, "UTF-8");
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email="
            + email + "]";
    }
}
