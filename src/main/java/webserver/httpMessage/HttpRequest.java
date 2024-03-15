package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String NO_QUERY_PARAMS = "";
    public static final String EMPTY_HTTP_REQUEST_ERROR = "빈 HTTP 요청입니다.";
    public static final String BLANK = " ";
    public static final String REQUEST_TARGET_DELIMITER = "?";

    private final List<String> httpRequest;
    private final String startLine;
    private final String requestTarget;

    public HttpRequest(List<String> httpRequest) {
        validate(httpRequest);
        this.httpRequest = httpRequest;
        this.startLine = httpRequest.get(0);
        this.requestTarget = getRequestTarget();
    }

    private void validate(List<String> httpRequest) {
        if (httpRequest.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_HTTP_REQUEST_ERROR);
        }
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<String> httpRequest = new ArrayList<>();

        String line;
        while (!(line = br.readLine()).isEmpty()) {
            httpRequest.add(line);
        }

        return new HttpRequest(httpRequest);
    }

    public void log() {
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
}
