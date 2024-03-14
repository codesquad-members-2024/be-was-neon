package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest();
    }

    @Test
    @DisplayName("request line을 공백 기준으로 나누어, 두 번째(인덱스1)에 해당하는 요청 target을 얻습니다.")
    void getRequestTarget() {
        String requestLine = "GET /registration/index.html HTTP/1.1";
        assertThat(httpRequest.getTarget(requestLine)).isEqualTo("/registration/index.html");
    }

    @Test
    @DisplayName("쿼리문을 \"&\" 기준으로 필드를 나누고 \"=\" 기준으로 key-value 형태로 나누어 파싱하고 그 결과를 Map에 담습니다.")
    void getQueryParsingResult() {
        String query = "userId=kkk12&name=park&password=1234&email=kkk12@naver.com";
        Map<String, String> parameters = httpRequest.parseQuery(query);
        assertThat(parameters.get("userId")).isEqualTo("kkk12");
        assertThat(parameters.get("name")).isEqualTo("park");
        assertThat(parameters.get("password")).isEqualTo("1234");
        assertThat(parameters.get("email")).isEqualTo("kkk12@naver.com");
    }
}
