package Utils;

import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP 요청을 파싱하여 다양한 정보를 추출하고, 요청된 URL의 경로에 따른 파일 경로를 생성하는 클래스
 */
public class HttpRequestParser {
    private String requestLine;
    private Map<String, String> headers;
    private String body;

    public HttpRequestParser(String httpRequest) {
        this.headers = new HashMap<>();
        String[] requestParts = httpRequest.split("\r\n", 2);
        parseRequestLineAndHeaders(requestParts[0]);
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

    public Optional<User> parseUserFromGetRequest() {
        try {
            String path = extractPath();
            if (!path.startsWith("/create")) {
                return Optional.empty();
            }

            String query = path.split("\\?", 2).length > 1 ? path.split("\\?", 2)[1] : "";
            if (query.isEmpty()) {
                return Optional.empty();
            }

            Map<String, String> queryParams = new HashMap<>();
            String[] params = query.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length < 2) {
                    continue; // 값이 없는 파라미터는 무시
                }
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParams.put(key, value);
            }

            User user = new User(
                    queryParams.getOrDefault("userId", ""),
                    queryParams.getOrDefault("password", ""),
                    queryParams.getOrDefault("name", ""),
                    queryParams.getOrDefault("email", "")
            );

            return Optional.of(user);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
