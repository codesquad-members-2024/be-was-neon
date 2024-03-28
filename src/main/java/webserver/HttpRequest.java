package webserver;

import Utils.HttpRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private String path;

    public HttpRequest(String httpRequest) throws IOException {
        parseRequest(httpRequest);
    }

    // 요청 문자열을 파싱하는 메소드
    private void parseRequest(String httpRequest) throws IOException {
        HttpRequestParser httpRequestParser = new HttpRequestParser(httpRequest);
        path = httpRequestParser.extractPath();
        logger.info("요청 경로: {}", path);
    }

    public String getPath() {
        return path;
    }
}
