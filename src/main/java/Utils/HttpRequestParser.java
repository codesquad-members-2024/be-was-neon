package Utils;

import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP 요청 문자열을 파싱하여 요청 라인, 헤더, 바디를 분석하는 클래스
 */
public class HttpRequestParser {
    private String requestLine;
    private Map<String, String> headers;
    private String body;

    /**
     * HttpRequestParser 생성자
     *
     * @param httpRequest HTTP 요청 전체 문자열
     */
    public HttpRequestParser(String httpRequest) throws UnsupportedEncodingException {
        this.headers = new HashMap<>();
        String[] requestParts = httpRequest.split("\r\n\r\n", 2);
        parseRequestLineAndHeaders(requestParts[0]);

        if (requestParts.length > 1) {
            this.body = URLDecoder.decode(requestParts[1], StandardCharsets.UTF_8);
        }
    }

    /**
     * 요청 라인과 헤더를 파싱
     * @param requestHeaders 요청 라인과 헤더를 포함한 문자열
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
     * 요청 라인에서 경로를 추출
     *
     * @return URL 경로 문자열
     */
    public String extractPath() {
        String[] tokens = this.requestLine.split(" ");
        if (tokens.length < 3) {
            throw new IllegalArgumentException("잘못된 요청 라인 형식입니다.");
        }
        return tokens[1];
    }

    /**
     * POST 요청의 바디에서 사용자 정보를 파싱하여 User 객체로 반환
     *
     * @return User 객체를 포함하는 Optional. 바디가 비어있거나 파싱할 수 없는 경우 빈 Optional 반환.
     */
    public Optional<User> parseUserFromBody() {
        if (this.body == null || this.body.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> formParams = splitFormParams(this.body);

        User user = new User(
                formParams.getOrDefault("userId", ""),
                formParams.getOrDefault("password", ""),
                formParams.getOrDefault("name", ""),
                formParams.getOrDefault("email", "")
        );

        return Optional.of(user);
    }

    /**
     * 바디 문자열을 파싱하여 폼 파라미터를 Map으로 변환
     *
     * @param body 폼 파라미터를 포함한 HTTP 요청 바디 문자열
     * @return 폼 파라미터 이름과 값을 연결한 Map
     */
    private Map<String, String> splitFormParams(String body) {
        Map<String, String> formParams = new HashMap<>();
        String[] params = body.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length < 2) {
                continue; // 값이 없는 파라미터는 무시
            }
            formParams.put(keyValue[0], keyValue[1]); // URLDecode 처리된 값이 사용됨
        }

        return formParams;
    }
}
