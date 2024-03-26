package Utils;

import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequestParser {
    private String requestLine;
    private Map<String, String> headers;
    private String body;

    public HttpRequestParser(String httpRequest) throws UnsupportedEncodingException {
        this.headers = new HashMap<>();
        String[] requestParts = httpRequest.split("\r\n\r\n", 2);
        parseRequestLineAndHeaders(requestParts[0]);

        if (requestParts.length > 1) {
            this.body = requestParts[1];
            this.body = URLDecoder.decode(this.body, "UTF-8");
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

    // POST 요청의 바디에서 User 객체를 파싱
    public Optional<User> parseUserFromBody() {
        if (this.body == null || this.body.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> formParams = new HashMap<>();
        String[] params = this.body.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length < 2) {
                continue; // 값이 없는 파라미터는 무시
            }
            String key = keyValue[0];
            String value = keyValue[1]; // 이미 URLDecode 처리됨
            formParams.put(key, value);
        }

        User user = new User(
                formParams.getOrDefault("userId", ""),
                formParams.getOrDefault("password", ""),
                formParams.getOrDefault("name", ""),
                formParams.getOrDefault("email", "")
        );

        return Optional.of(user);
    }
}