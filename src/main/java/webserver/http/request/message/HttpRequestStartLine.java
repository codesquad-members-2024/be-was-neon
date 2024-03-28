package webserver.http.request.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestStartLine {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestStartLine.class);

    private HttpMethod httpMethod;
    private String requestLine;
    private String httpVersion;

    public HttpRequestStartLine(String startLine) {
        setStartLine(startLine);
        logger.debug("Request : {}", startLine);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String toString() {
        return httpMethod.getMethodDescription() + " " + requestLine + " " + httpVersion;
    }

    private void setStartLine(String startLine) {
        String[] tokens = startLine.split(" ");
        this.httpMethod = HttpMethod.valueOf(tokens[0]);
        this.requestLine = tokens[1];
        this.httpVersion = tokens[2];
    }
}
