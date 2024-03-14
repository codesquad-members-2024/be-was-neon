package utils;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

import static java.net.URLDecoder.decode;

public class RegistrationResponse {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationResponse.class);
    public static void getUserData(DataOutputStream dos, String fileName){
        try {
            String toLogin = "/login/index.html";
            String[] parts = fileName.split("\\?"); // "?" 이후의 정보을 불러오기위해
            String path = parts[0];
            String queryParams = parts[1];
            String[] params = queryParams.split("&");
            String username = "";
            String nickname = "";
            String password = "";

            for (String param : params) {
                String[] keyValue = param.split("="); // "="를 기준으로 파라미터를 키-값으로 나눕니다.
                String key = keyValue[0];
                String value = keyValue[1];

                // 키에 따라 값을 설정합니다.
                if (key.equals("username")) {
                    username = value;
                } else if (key.equals("nickname")) {
                    nickname = decode(value,"UTF-8");
                } else if (key.equals("password")) {
                    password = value;
                }
            }
            User user = new User(username, password, nickname);
            logger.debug("신규 유저가 생성되었습니다. {}", user);
            Database.addUser(user);
            logger.debug("모든 유저 {}", Database.findAll());
            response302Header(dos,toLogin);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String location){
        // 302 response 는 get 으로 유저 데이터를 받아온후에 redirect 하여 클라이언트를 로그인 페이지로 연결시켜줍니다.
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
