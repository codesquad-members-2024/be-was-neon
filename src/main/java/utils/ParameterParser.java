package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ParameterParser {

    public static Map<String, String> getUserParams(String input) {
        Map<String, String> params = new HashMap<>();
        for (String param : input.split("&")) {
            String[] keyValue = param.split("=");
            try {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return params;
    }
}