package userservice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.net.URLDecoder.decode;

public class UserDataParser {
    // 유저 정보를 문자열로 받아 맵으로 반환한다.
    public static Map<String, String> extractUserData(String userData) throws UnsupportedEncodingException {
        String[] params = userData.split("&");
        Map<String, String> paramMap = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("="); // "="를 기준으로 파라미터를 키-값으로 나눕니다.
            paramMap.put(keyValue[0],decode(keyValue[1],"UTF-8"));
        }
        return paramMap;
    }
}
