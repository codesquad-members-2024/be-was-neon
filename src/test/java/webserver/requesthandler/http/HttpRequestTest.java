package webserver.requesthandler.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpCookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    @DisplayName("request에 저장된 쿠키를 가져올 수 있다.")
    void getCookie() {
        HttpRequest request = new HttpRequest();
        request.setHeader("Cookie", "SID=123456");

        HttpCookie cookie = request.getCookie("SID");
        assertThat(cookie.getValue()).isEqualTo("123456");
    }
}