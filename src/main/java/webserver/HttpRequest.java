package webserver;

import Utils.HttpRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String path;

    public HttpRequest(String httpRequest) throws IOException {
        HttpRequestParser httpRequestParser = new HttpRequestParser(httpRequest);
        path = httpRequestParser.extractPath(); // HttpRequestParser를 사용하여 요청 경로 추출
        logger.info("요청 경로: {}", path);
    }

    public String getPath() {
        return path;
    }
}
