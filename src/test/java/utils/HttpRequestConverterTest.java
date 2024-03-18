package utils;

import static org.assertj.core.api.Assertions.*;

import http.HttpMethod;
import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestConverterTest {

    @DisplayName("HTTP request header로 부터 HttpRequest를 생성할 수 있다")
    @Test
    void convert() {
        // given
        String header = """
                GET /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Referer: "https://www.google.com/"
                Pragma: no-cache
                Cache-Control: no-cache
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
                Accept-Encoding: gzip, deflate, br, zstd
                Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7
                Cookie: Idea-fcd223d4=b6a3f2fa-6bf3-46d9-9244-dc44850cb75f;
                If-Modified-Since: none;
                If-None-Match: none;
                """;
        // when
        HttpRequest httpRequest = HttpRequestConverter.convert(header);

        // then
        /* request line */
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getRequestURI()).isEqualTo("/index.html");
        assertThat(httpRequest.getHttpVersion()).isEqualTo("HTTP/1.1");

        /* headers */
        assertThat(httpRequest.getHost()).isEqualTo("Host: localhost:8080");
        assertThat(httpRequest.getConnection()).isEqualTo("Connection: keep-alive");
        assertThat(httpRequest.getReferer()).isEqualTo("Referer: \"https://www.google.com/\"");
        assertThat(httpRequest.getUserAgent()).isEqualTo("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        assertThat(httpRequest.getAccept()).isEqualTo("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        assertThat(httpRequest.getAcceptEncoding()).isEqualTo("Accept-Encoding: gzip, deflate, br, zstd");
        assertThat(httpRequest.getAcceptLanguage()).isEqualTo("Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7");

        assertThat(httpRequest.getCookie()).isEqualTo("Cookie: Idea-fcd223d4=b6a3f2fa-6bf3-46d9-9244-dc44850cb75f;");
        assertThat(httpRequest.getCacheControl()).isEqualTo("Cache-Control: no-cache");
        assertThat(httpRequest.getPragma()).isEqualTo("Pragma: no-cache");
        assertThat(httpRequest.getIfModifiedSince()).isEqualTo("If-Modified-Since: none;");
        assertThat(httpRequest.getIfNoneMatch()).isEqualTo("If-None-Match: none;");
    }

    @DisplayName("post 요청일 때 http body인 'id=yelly&password=myPassword&username=testName&email=test@test.com'를 쿼리 파라미터로 가져올 수 있다")
    @Test
    void post_convert() {
        // given
        String header = """
                POST /registration HTTP/1.1
                Host: localhost:8080
                Content-Length: 66
                id=yelly&password=myPassword&username=testName&email=test@test.com""";

        // when
        HttpRequest httpRequest = HttpRequestConverter.convert(header);

        // then
        assertThat(httpRequest.getParameter("id")).isEqualTo("yelly");
        assertThat(httpRequest.getParameter("password")).isEqualTo("myPassword");
        assertThat(httpRequest.getParameter("username")).isEqualTo("testName");
        assertThat(httpRequest.getParameter("email")).isEqualTo("test@test.com");
    }
}