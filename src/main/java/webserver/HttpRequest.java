package webserver;

import Utils.HttpRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final HttpRequestReader reader;
    private String path;

    public HttpRequest(HttpRequestReader reader) throws IOException {
        this.reader = reader;
        parseRequest();
    }

    private void parseRequest() throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (isFirstLine) {
                logger.info("요청 라인: {}", line);
                isFirstLine = false;
            }
            requestBuilder.append(line).append("\r\n");
        }
        String httpRequest = requestBuilder.toString();
        HttpRequestParser httpRequestParser = new HttpRequestParser(httpRequest);
        path = httpRequestParser.extractPath();
    }

    public String getPath() {
        return path;
    }
}
