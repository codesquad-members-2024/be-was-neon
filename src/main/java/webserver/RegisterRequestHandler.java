package webserver;

import db.Database;
import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class RegisterRequestHandler {
    RegisterRequestHandler(String startLine) {
        storeDatabase(parseRegisterRequest(startLine));
    }
    private User parseRegisterRequest(String startLine) {
        String[] splitLine = startLine.split("[=& ]");
        String userId = "", password = "", name = "", email = "";
        try {
            // 디코딩하여 한글로 변환
            userId = URLDecoder.decode(splitLine[2], "UTF-8");
            password = URLDecoder.decode(splitLine[4], "UTF-8");
            name = URLDecoder.decode(splitLine[6], "UTF-8");
            email = URLDecoder.decode(splitLine[8], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new User(userId, password, name, email);
    }

    private void storeDatabase(User user){
        Database.addUser(user);
    }

}
