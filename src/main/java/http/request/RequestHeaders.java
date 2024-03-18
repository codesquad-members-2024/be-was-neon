package http.request;

import static utils.Constant.LINE_FEED;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestHeaders {
    public static final String HEADER_DELIMITER = ":\s*";
    private static final int HEADER_DELIMITER_LIMIT_COUNT = 2;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private final Map<String, String> headers;

    public RequestHeaders(String headers) {
        this.headers = new LinkedHashMap<>();
        initHeaders(headers);
    }

    private void initHeaders(String headers) {
        String[] header = headers.split(LINE_FEED);
        for (String line : header) {
            String[] keyNValue = line.split(HEADER_DELIMITER, HEADER_DELIMITER_LIMIT_COUNT);
            this.headers.put(keyNValue[KEY_INDEX], keyNValue[VALUE_INDEX]);
        }
    }

    public String findHeader(String key) {
        return this.headers.get(key);
    }
}
