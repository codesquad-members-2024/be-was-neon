package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.net.URLDecoder.decode;

public class RegistrationHandler {

    public static Map<String, String> extractUserData(String fileName) throws UnsupportedEncodingException {
        String[] params = fileName.split("&");

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
}
