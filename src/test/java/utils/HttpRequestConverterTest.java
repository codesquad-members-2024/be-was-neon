package utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestConverterTest {

    @DisplayName("HTTP request header로 부터 HttpRequest를 생성할 수 있다")
    @Test
    void convert() {
        // given
        String header = "GET /index.html HTTP/1.1"
                + "Host: localhost:8080"
                + "Connection: keep-alive"
                + "Pragma: no-cache"
                + "Cache-Control: no-cache"
                + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
                + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                + "Accept-Encoding: gzip, deflate, br, zstd"
                + "Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7"
                + "Cookie: Idea-fcd223d4=b6a3f2fa-6bf3-46d9-9244-dc44850cb75f;";
        // when
        HttpRequest httpRequest = HttpRequestConverter.convert(header);

        // then
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getRequestURI()).isEqualTo("/index.html");
        assertThat(httpRequest.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequest.getRequestURI()).isEqualTo("HTTP/1.1");
    }
}