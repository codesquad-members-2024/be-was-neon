// Utils 패키지에 위치한 HTTPRequestParser 클래스
package Utils;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequestParser {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public void parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("잘못된 요청 라인 형식입니다. 올바른 형식: 메소드 URL 프로토콜/버전");
        }

        this.method = tokens[0];
        this.path = tokens[1];
        this.version = tokens[2];
    }

    public void parseHeaders(String[] headers) {
        for (String header : headers) {
            String[] keyValue = header.split(": ");
            if (keyValue.length == 2) {
                this.headers.put(keyValue[0].trim(), keyValue[1].trim());
            } else {
                throw new IllegalArgumentException("잘못된 헤더 형식입니다. 올바른 형식: 키: 값");
            }
        }
    }

    public void parseBody(String body) {
        this.body = body;
    }

    // 기타 필요한 메소드 구현

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getVersion() {
        return this.version;
    }

    public String getHeaderValue(String key) {
        return headers.get(key);
    }
}
