package httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestLineParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    private String requestLine;
    private Map<String, String> headers = new HashMap<>();
    private String body;// ⭐HttpBody는 Map 형식으로 하기 어려움 -> 텍스트와 바이너리 데이터 모두 저장할 수 있는 byte[] 이나 String 으로 수정 예정

    /* */

    private Map<String, String> formData = new HashMap<>();
    private Map<String, String> queries = new HashMap<>(); // RequestLineParser composition
    private String requestURL; // RequestLineParser composition
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    // ⭐HttpRequest 는 다른 클래스에서 받아 결과적으로 만들어지는 데이터 구조를 가지는 클래스
    // 그 다른 클래스가 어떤 클래스인지 생성할 때 결정하지 않아도 됨 -> HttpRequest 를 생성하기 위해 필요한 값만 받으면 생성할 수 있어야 함
    // 그래야 테스트 관점에서도 HttpRequest 만 생성하고 테스트하기 수월하다
    // 즉, 다양한 시나리오를 수용할 수 있도록 유연성을 높여야함! 현재는 InputStream 을 통해서만 생성가능...
    // 생성자 오버 로딩 : 생성자 여러 개 제공
    // 빌더 패턴 : 객체 생성 과정을 단계별로 수행할 수 있게 해줌
    public HttpRequest(InputStream in) throws IOException { // HttpRequest 객체 생성될 때 초기화 ...
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        this.requestLine = br.readLine(); // 요청 라인
        readHeaders(br); // 헤더

        // Content-Length 헤더 참고해서 본문 길이 얻기 -> 이를 이용해 본문 데이터 읽기
        // Content-Length? 표현 데이터의 길이 (바이트단위) ex) 본문 hello, Content-Length : 5
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length")); // 41, 44 ...
            char[] bodyChars = new char[contentLength];
            br.read(bodyChars, 0, contentLength); // 0부터 contentLength 까지 bodyChars 에 저장
            this.body = new String(bodyChars);
            parseFormData(this.body);
        }

        RequestLineParser requestLineParser = new RequestLineParser(this.requestLine);
        this.requestURL = requestLineParser.getRequestURL(); // 요청 URL 저장
        this.queries = requestLineParser.getQueries(); // 쿼리 파라미터 저장
    }

    private void parseFormData(String body) {
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            formData.put(key, value);
        }
    }

    private void readHeaders(BufferedReader br) throws IOException {
        String line = requestLine;
        while ((line = br.readLine()) != null && !line.isEmpty()) { // 첫 번째 라인 (요청 라인) 은, 헤더가 아니기에 건너뛰고 시작한다.
            int separator = line.indexOf(":");
            if (separator != -1) {
                String name = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();
                headers.put(name, value);
            }
        }
    }

    public void printHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            logger.debug("Header Key: \"{}\" Value: \"{}\"", header.getKey(), header.getValue());
        }
    }

    // ⭐내부 속성들을 전달해주는 getter를 가지는 경우라면 각각의 타입을 만들어 보는 것 고려해보기
    // 예 : RequestLine, Headers, Body, Queires 등 등 클래스를 만들면 어떨까 고려해보기

    public String getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public Map<String, String> getFormData() {
        return formData;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getValue(String key) {
        return getFormData().get(key);
    }
}