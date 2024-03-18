package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String NO_QUERY_PARAMS = "";
    public static final String BLANK = " ";
    public static final String REQUEST_TARGET_DELIMITER = "?";
    public static final String CONTENT_LENGTH = "Content-Length";

    private final String startLine;
    private final String requestTarget;
    private final Map<String, String> header;
    private final Map<String, String> body;

    public HttpRequest(String startLine, Map<String, String> header, Map<String, String> body) {
        this.startLine = startLine;
        this.requestTarget = getRequestTarget();
        this.header = header;
        this.body = body;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String startLine = br.readLine();
        Map<String, String> header = readHeader(br);
        Map<String, String> body = readBody(br, header.get(CONTENT_LENGTH));
        return new HttpRequest(startLine, header, body);
    }

    private static Map<String, String> readBody(BufferedReader br, String contentLength) throws IOException {
        if (contentLength == null) {
            return Map.of();
        }

        String body = readBodyContent(br, contentLength);
        return HttpRequestParser.parseKeyValuePairs(body);
    }

    private static String readBodyContent(BufferedReader br, String contentLength) throws IOException {
        StringBuilder bodyBuffer = new StringBuilder();
        int length = Integer.parseInt(contentLength);
        for (int i = 0; i < length; i++) {
            bodyBuffer.append((char) br.read());
        }
        return bodyBuffer.toString();
    }

    private static Map<String, String> readHeader(BufferedReader br) throws IOException {
        StringBuilder headerBuffer = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            headerBuffer.append(line);
        }
        return HttpRequestParser.parseKeyValuePairs(headerBuffer.toString());
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

    public Map<String, String> getBody() {
        return body;
    }
}
