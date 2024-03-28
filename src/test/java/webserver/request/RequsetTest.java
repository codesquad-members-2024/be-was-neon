package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestLine;
import request.RequestLineParser;

import static org.assertj.core.api.Assertions.assertThat;

public class RequsetTest {
    @Test
    @DisplayName("Request line 에서 requestMethod 를 추출하였습니다.")
    void getRequestMethodTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMethod = "GET";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getMethod()).isEqualTo(expectedMethod);
    }

    @Test
    @DisplayName("Request line 에서 requestPath 를 추출하였습니다.")
    void getRequestPathTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedPath = "/index.html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getPath()).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("Request line 에서 path 에 있는 file extension 을 통해 mimeType 를 반환하였습니다.")
    void getMimeTypeTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMimeType = "text/html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getMimeType()).isEqualTo(expectedMimeType);
    }

    @Test
    @DisplayName("Request line 에서 requestPath 를 추출할때 index.html 를 붙여서 받아오도록 해주었습니다.")
    void getUnnamedPathTest() {
        String givenRequestLine = "GET / HTTP/1.1";
        String expectedPath = "/index.html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getPath()).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("Request GET으로 로그인 정보를 가져왔을때 QueryParameter 부분을 추출 해주었습니다.")
    void getQueryParamTest() {
        RequestLineParser requestLineParser = new RequestLineParser();
        String loginRequest = "GET /user?userId=MirId&password=password HTTP/1.1";
        String expectedQueryParam = "userId=MirId&password=password";
        RequestLine requestLine = requestLineParser.parse(loginRequest);
        assertThat(requestLine.getQueryParam()).isEqualTo(expectedQueryParam);
    }

}
