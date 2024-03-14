package model;

import db.Database;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UserRegistration {

    public static void registration(String queryString) throws UnsupportedEncodingException {
        StringTokenizer tokenizer = new StringTokenizer(queryString,"&");
        HashMap<String,String> queryMap = new HashMap();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            queryMap.put(keyValue[0], keyValue[1]);
        }

        User user =
            new User(queryMap.get("id"), queryMap.get("nickname"), queryMap.get("password"), queryMap.get("email"));

        Database.addUser(user);
    }

}
