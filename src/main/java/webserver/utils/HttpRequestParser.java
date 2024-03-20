package webserver.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {

    public static final String QUERY_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    public static final String HEADER_DELIMITER = ":?\\s";

    public static Map<String, String> parseKeyValuePairs(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = query.split(QUERY_DELIMITER);

        for (String pair : pairs) {
            String[] split = pair.split(KEY_VALUE_DELIMITER);
            queryPairs.put(split[0], split[1]);
        }

        return queryPairs;
    }

    public static Map<String, String> parseHeader(List<String> header) {
        Map<String, String> parsedHeader = new HashMap<>();

        for (String line : header) {
            String[] split = line.split(HEADER_DELIMITER);
            parsedHeader.put(split[0], split[1]);
        }
        return parsedHeader;
    }
}
