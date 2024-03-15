package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);
    public static User requestLineParser(String requestLine) {
        String url = extractRequestURL(requestLine);
        // URL에서 경로와 쿼리 파라미터를 추출
        int queryIndex = url.indexOf('?');
        // ? 이전 추출
        String path = queryIndex != -1 ? url.substring(0, queryIndex) : url;
        // ? 이후 url 추출
        String query = queryIndex != -1 ? url.substring(queryIndex + 1) : "";

        // 쿼리 파라미터를 파싱하여 Map 형태로 저장
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }

        // 파싱한 값을 이용하여 User 객체를 생성
        String userId = queryParams.getOrDefault("userId", "");
        String password = queryParams.getOrDefault("password", "");
        String name = queryParams.getOrDefault("nickName", "");
        Database.addUser(new User(userId, password, name));
        logger.debug("new user : {}", Database.findUserById(userId));
        return new User(userId, password, name);
    }
    public static String extractRequestURL(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            return null;
        }
        return tokens[1];
    }
}
