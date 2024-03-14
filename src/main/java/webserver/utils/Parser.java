package webserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static final String UTF_8 = "UTF-8";
    public static final String QUERY_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new HashMap<>();
        String[] pairs = query.split(QUERY_DELIMITER);

        for (String pair : pairs) {
            String[] split = pair.split(KEY_VALUE_DELIMITER);
            query_pairs.put(URLDecoder.decode(split[0], UTF_8), URLDecoder.decode(split[1], UTF_8));
        }

        return query_pairs;
    }
}
