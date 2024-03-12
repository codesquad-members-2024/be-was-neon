package utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestHeaderParserTest {

    @DisplayName("GET 요청에 대해 header를 파싱하면 요청한 URL을 반환한다")
    @Test
    void requestParse() {
        // given
        String url = "GET /index.html HTTP/1.1";

        // when
        String request = RequestHeaderParser.requestParse(url);

        // then
        assertThat(request).isEqualTo("/index.html");
    }
}