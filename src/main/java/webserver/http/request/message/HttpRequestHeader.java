package webserver.http.request.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpRequestHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeader.class);

    private final Map<String, String> headers;

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
    }

    public String getValue(String key) {
        return headers.get(key);
    }

}
