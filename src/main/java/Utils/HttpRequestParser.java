package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청을 파싱하여 다양한 정보를 추출하는 클래스
 */
public class HttpRequestParser {

    private String requestLine;
    private Map<String, String> headers;
    private String body;

    public HttpRequestParser(String httpRequest) {
        this.headers = new HashMap<>();
        parseRequest(httpRequest);
    }

    /**
     * HTTP 요청을 파싱하는 메소드
     *
     * @param httpRequest 전체 HTTP 요청 문자열
     */
    private void parseRequest(String httpRequest) {
        String[] requestParts = httpRequest.split("\r\n\r\n", 2);
        parseRequestLineAndHeaders(requestParts[0]);
        if (requestParts.length > 1) {
            this.body = requestParts[1];
        }
    }

    /**
     * 요청 라인과 헤더를 파싱하는 메소드
     *
     * @param requestHeaders 요청 라인과 헤더를 포함하는 문자열
     */
    private void parseRequestLineAndHeaders(String requestHeaders) {
        String[] lines = requestHeaders.split("\r\n");
        this.requestLine = lines[0];

        for (int i = 1; i < lines.length; i++) {
            String[] headerParts = lines[i].split(": ", 2);
            this.headers.put(headerParts[0], headerParts[1]);
        }
    }

    /**
     * 요청 라인에서 요청 URL을 추출하는 메소드
     *
     * @return 요청된 URL의 경로
     */
    public String extractPath() {
        String[] tokens = this.requestLine.split(" ");
        if (tokens.length < 3) {
            throw new IllegalArgumentException("잘못된 요청 라인 형식입니다.");
        }
        return tokens[1];
    }

    // 필요에 따라 추가적인 메소드 구현 가능
    // 예: getHeaders(), getBody(), getRequestMethod() 등

}
