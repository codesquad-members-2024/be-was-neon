package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청을 파싱하여 다양한 정보를 추출하고, 요청된 URL의 경로에 따른 파일 경로를 생성하는 클래스
 */
public class HttpRequestParser {
    private String requestLine;
    private Map<String, String> headers;
    private String body;
    private static final String BASIC_FILE_PATH = "src/main/resources/static/";
    private static final String INDEX_FILE_NAME = "/index.html";
    private static final String REGISTER_ACTION = "/registration";
    private static final String LOGIN_ACTION = "/login";

    public HttpRequestParser(String httpRequest) {
        this.headers = new HashMap<>();
        parseRequest(httpRequest);
    }

    private void parseRequest(String httpRequest) {
        String[] requestParts = httpRequest.split("\r\n\r\n", 2);
        parseRequestLineAndHeaders(requestParts[0]);
        if (requestParts.length > 1) {
            this.body = requestParts[1];
        }
    }

    private void parseRequestLineAndHeaders(String requestHeaders) {
        String[] lines = requestHeaders.split("\r\n");
        this.requestLine = lines[0];

        for (int i = 1; i < lines.length; i++) {
            String[] headerParts = lines[i].split(": ", 2);
            this.headers.put(headerParts[0], headerParts[1]);
        }
    }

    public String extractPath() {
        String[] tokens = this.requestLine.split(" ");
        if (tokens.length < 3) {
            throw new IllegalArgumentException("잘못된 요청 라인 형식입니다.");
        }
        return tokens[1];
    }

    public String makeCompletePath() {
        String requestURL = extractPath();
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        switch (requestURL) {
            case REGISTER_ACTION:
                // registration 요청에 대한 처리
                completePath.append("/registration").append(INDEX_FILE_NAME);
                break;
            case LOGIN_ACTION:
                // login 요청에 대한 처리
                completePath.append("/login").append(INDEX_FILE_NAME);
                break;
            default:
                if (requestURL.equals("/")) {
                    completePath.append(INDEX_FILE_NAME);
                } else {
                    completePath.append(requestURL);
                    if (!completePath.toString().endsWith(".html")) {
                        // 확장자 처리, 필요에 따라 다른 파일 유형도 처리 가능
                        completePath.append(".html");
                    }
                }
                break;
        }

        return completePath.toString();
    }


    // 필요에 따라 추가적인 메소드 구현 가능
    // 예: getHeaders(), getBody(), getRequestMethod() 등
}
