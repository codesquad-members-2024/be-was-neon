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
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";
    private static final String REGISTER_PAGE = "/registration";
    private static final String LOGIN_PAGE = "/login";

    public HttpRequestParser(String httpRequest) {
        this.headers = new HashMap<>();
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

    public String makePath() {
        String requestURL = extractPath();
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        switch (requestURL) {
            case REGISTER_PAGE:
                completePath.append("/registration").append(INDEX_FILE_NAME);
                break;
            case LOGIN_PAGE:
                completePath.append("/login").append(INDEX_FILE_NAME);
                break;
            default:
                if (requestURL.equals("/")) {
                    completePath.append(INDEX_FILE_NAME);
                } else {
                    completePath.append(requestURL);
                    if (!requestURL.contains(".")) {
                        completePath.append(".html"); // 기본적으로 .html 추가
                    } else {
                        String extension = requestURL.substring(requestURL.lastIndexOf("."));
                        switch (extension) {
                            case ".css":
                            case ".js":
                            case ".ico":
                            case ".png":
                            case ".jpg":
                            case ".svg":
                                break;
                            default:
                                // 지원하지 않는 확장자 로직
                                break;
                        }
                    }
                }
                break;
        }
        return completePath.toString();
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
