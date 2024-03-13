package utils;

import static org.assertj.core.api.Assertions.*;
import static utils.HttpHeaderParser.*;

import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpHeaderParserTest {

    @DisplayName("\"GET /home.html HTTP/1.1\nHost: developer.mozilla.org\"를 파싱하면 request line을 추출할 수 있다.")
    @Test
    void parse_request_line() {
        // given
        String requestHeader = "GET /home.html HTTP/1.1\nHost: developer.mozilla.org";

        // when
        String requestLine = REQUEST_LINE.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("GET /home.html HTTP/1.1");
    }

    @DisplayName("\"GET /home.html HTTP/1.1\nHost: developer.mozilla.org\"를 파싱하면 host를 추출할 수 있다.")
    @Test
    void parse_host() {
        // given
        String requestHeader = "GET /home.html HTTP/1.1\nHost: developer.mozilla.org";

        // when
        String requestLine = HOST.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Host: developer.mozilla.org");
    }

    @DisplayName("\"Host: developer.mozilla.org\nUser-Agent: Mozilla/5.0 Macintosh Firefox/50.0\"를 파싱하면 user-agent를 추출할 수 있다.")
    @Test
    void parse_user_agent() {
        // given
        String requestHeader = "Host: developer.mozilla.org\nUser-Agent: Mozilla/5.0 Macintosh Firefox/50.0";

        // when
        String requestLine = USER_AGENT.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("User-Agent: Mozilla/5.0 Macintosh Firefox/50.0");
    }

    @DisplayName("\"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\"를 파싱하면 기본 accept를 추출할 수 있다.")
    @Test
    void parse_accept() {
        // given
        String requestHeader = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5";

        // when
        String requestLine = ACCEPT.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    }

    @DisplayName("\"Accept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br\"를 파싱하면 accept-language를 추출할 수 있다.")
    @Test
    void parse_accept_language() {
        // given
        String requestHeader = "Accept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br";

        // when
        String requestLine = ACCEPT_LANGUAGE.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Accept-Language: en-US,en;q=0.5");
    }

    @DisplayName("\"Accept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br\"를 파싱하면 accept-encoding을 추출할 수 있다.")
    @Test
    void parse_accept_encoding() {
        // given
        String requestHeader = "Accept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br";

        // when
        String requestLine = ACCEPT_ENCODING.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Accept-Encoding: gzip, deflate, br");
    }

    @DisplayName("\"Referer: https://developer.mozilla.org/testpage.html\nConnection: keep-alive\"를 파싱하면 referer를 추출할 수 있다.")
    @Test
    void parse_referer() {
        // given
        String requestHeader = "Referer: https://developer.mozilla.org/testpage.html\nConnection: keep-alive";

        // when
        String requestLine = REFERER.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Referer: https://developer.mozilla.org/testpage.html");
    }

    @DisplayName("\"Referer: https://developer.mozilla.org/testpage.html\nConnection: keep-alive\"를 파싱하면 connection을 추출할 수 있다.")
    @Test
    void parse_connection() {
        // given
        String requestHeader = "Referer: https://developer.mozilla.org/testpage.html\nConnection: keep-alive";

        // when
        String requestLine = CONNECTION.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Connection: keep-alive");
    }

    @DisplayName("\"If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT\nIf-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"\"를 파싱하면 if_modified_since을 추출할 수 있다.")
    @Test
    void parse_if_modified_since() {
        // given
        String requestHeader = "If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT\nIf-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"";

        // when
        String requestLine = IF_MODIFIED_SINCE.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT");
    }

    @DisplayName("\"If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT\nIf-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"\"를 파싱하면 if_none_match를 추출할 수 있다.")
    @Test
    void parse_if_none_match() {
        // given
        String requestHeader = "If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT\nIf-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"";

        // when
        String requestLine = IF_NONE_MATCH.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("If-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"");
    }

    @DisplayName("\"If-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"\nCache-Control: max-age=0\"를 파싱하면 cache_control을 추출할 수 있다.")
    @Test
    void parse_cache_control() {
        // given
        String requestHeader = "If-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"\nCache-Control: max-age=0";

        // when
        String requestLine = CACHE_CONTROL.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Cache-Control: max-age=0");
    }

    @DisplayName("\"Cache-Control: max-age=0\nCookie: _ga=GA1.1.1443472205.1698820297;\"를 파싱하면 cookie 추출할 수 있다.")
    @Test
    void parse_cookie() {
        // given
        String requestHeader = "Cache-Control: max-age=0\nCookie: _ga=GA1.1.1443472205.1698820297;";
        // when
        String requestLine = COOKIE.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Cookie: _ga=GA1.1.1443472205.1698820297;");
    }
    @DisplayName("\"Cache-Control: max-age=0\nPragma: no-cache\"를 파싱하면 pragma 추출할 수 있다.")
    @Test
    void parse_pragma() {
        // given
        String requestHeader = "Cache-Control: max-age=0\nPragma: no-cache";
        // when
        String requestLine = PRAGMA.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("Pragma: no-cache");
    }

    @DisplayName("\"\\*:\\*\" 형태의 헤더를 파싱하면 헤더를 그대로 추출할 수 있다.")
    @Test
    void parse_other() {
        // given
        String requestHeader = "아무거나: 아무거나 어쩌구";

        // when
        String requestLine = OTHER.parse(requestHeader);

        // then
        assertThat(requestLine).isEqualTo("아무거나: 아무거나 어쩌구");
    }

    @DisplayName("'/index.html?name=str&id=str2&money=123'에서 쿼리파라미터를 추출하면 name=str, id=str2, money=123 3개 이다")
    @Test
    void parse_query_params() {
        // given
        String requestHeader = "/index.html?name=str&id=str2&money=123";

        // when
        Map<String, String> params = parseParams(requestHeader);

        // then
        assertThat(params.size()).isEqualTo(3);
        assertThat(params).containsEntry("name", "str")
                .containsEntry("id", "str2")
                .containsEntry("money", "123");
        assertThat(params).doesNotContainEntry("noKey", "noValue");
    }
}