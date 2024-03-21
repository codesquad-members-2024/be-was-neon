package util;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static int KEY = 0;
    public static int VALUE = 1;
    public static String[] splitColon(String tempRequest) {
//        HTTP header를 분리한다.
        String[] headers = tempRequest.split(":", 2);
        String[] results = new String[headers.length];
        results[KEY] = headers[KEY].trim();
        results[VALUE] = headers[VALUE].trim();
        return results;
    }

    public static String getURI(String startRequest) {
        // GET /index.html HTTP/1.1 or GET /create?userId=ddd&name=blah&password=blah
        String[] startLines = startRequest.split(" ");
        return startLines[1];
    }

    public static Map<String, String> getUserInfo(String startLine) {
        Map<String, String> user = new HashMap<>();
        int queryStartIndex = startLine.indexOf("?") + 1;
//        query=userId=ddd&name=blah&password=blah
        String query = startLine.substring(queryStartIndex);
        String[] userInfo = query.split("&");
        for (String infoLine : userInfo) {
            String[] keyValue = infoLine.split("=");
            user.put(keyValue[0], keyValue[1]);
        }
        return user;
    }
}
