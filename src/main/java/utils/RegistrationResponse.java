package utils;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.net.URLDecoder.decode;

public class RegistrationResponse {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationResponse.class);
    private static final String LOGIN_URL = "/login/index.html";
    public static void respondRegistration(DataOutputStream dos, String fileName){
        try{
            Map<String, String> paramMap = getUserData(fileName);
            createUser(paramMap);
            response302Header(dos,LOGIN_URL);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public static Map<String, String> getUserData(String fileName) throws UnsupportedEncodingException {
        String[] parts = fileName.split("\\?"); // "?" 이후의 정보을 불러오기위해
        String queryParams = parts[1];
        String[] params = queryParams.split("&");

        Map<String, String> paramMap = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("="); // "="를 기준으로 파라미터를 키-값으로 나눕니다.
            if (keyValue[0].equals("nickname")){
                paramMap.put(keyValue[0],decode(keyValue[1],"UTF-8")); // nickname 을 get 해올때 한글인 경우 16진법으로 변환된 파일을 다시 한글로 decode 해준다.
            }else{
                paramMap.put(keyValue[0],keyValue[1]);
            }
        }
        return paramMap;
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
    private static void createUser(Map<String, String> paramMap){
        User user = new User(paramMap.get("username"), paramMap.get("password"), paramMap.get("nickname"));
        logger.debug("신규 유저가 생성되었습니다. {}", user);
        Database.addUser(user);
        logger.debug("모든 유저 {}", Database.findAll());
    }
}
