package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String httpMethod;
    private final String requestTarget;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String httpMethod, String requestTarget, Map<String, String> headers, String body) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.headers = headers;
        this.body = body;
    }

    public String getHttpMethod() {
        return httpMethod;
    }


    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHeaderValue(String data) {
        return headers.get(data);
    }

    public Map<String, String> parseData() throws IOException {
        Map<String, String> parsedData = new HashMap<>();

        // 데이터를 분리
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            // URL 디코딩하여 저장
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");

            parsedData.put(key, value);
        }
        return parsedData;
    }
}
