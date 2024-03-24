package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String NO_QUERY_PARAMS = "";
    public static final String BLANK = " ";
    public static final String REQUEST_TARGET_DELIMITER = "?";
    public static final String COOKIE = "Cookie";
    public static final String SID = "sid";


    private final String startLine;
    private final String requestTarget;
    private final Map<String, String> header;
    private final String body;

    public HttpRequest(String startLine, Map<String, String> header, String body) {
        this.startLine = startLine;
        this.requestTarget = getRequestTarget();
        this.header = header;
        this.body = body;

        logger.info(startLine);
    }

    public String getUri() {
        if (requestTarget.contains(REQUEST_TARGET_DELIMITER)) {
            String[] split = requestTarget.split("\\?");
            return split[0];
        }
        return requestTarget;
    }

    public String getQueryParams() {
        if (requestTarget.contains(REQUEST_TARGET_DELIMITER)) {
            String[] split = requestTarget.split("\\?");
            return split[1];
        }
        return NO_QUERY_PARAMS;
    }

    private String getRequestTarget() {
        String[] split = startLine.split(BLANK);
        return split[1];
    }

    public String getBody() {
        return body;
    }

    public String getSessionId() {
        String cookie = header.get(COOKIE);
        Map<String, String> parseCookie = HttpRequestParser.parseCookie(cookie);
        return parseCookie.get(SID);
    }
}
