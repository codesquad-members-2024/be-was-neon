package http;

import static org.assertj.core.api.Assertions.*;

import http.HttpRequest.HttpMethod;
import http.HttpRequest.HttpRequestUri;
import http.HttpRequest.HttpVersion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @DisplayName("요청 헤더에 존재하는 Cookie: SID=test-test; myCookie=myValue; 를 찾을 수 있다")
    @Test
    void getCookie() {
        // given
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SID=test-test; myCookie=myValue;");

        HttpRequest request = new HttpRequest(
                HttpMethod.GET, new HttpRequestUri("test"), new HttpVersion("HTTP/1.1"),
                headers, new HashMap<>()
        );

        // when
        List<Cookie> cookies = request.getCookie();

        // then
        assertThat(cookies.size()).isEqualTo(2);
        assertThat(cookies).extracting("cookieKey").contains("SID", "myCookie");
        assertThat(cookies).extracting("cookieValue").contains("test-test; ", "myValue; ");
    }
}