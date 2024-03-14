package utils;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*; // AssertJ

class StringUtilsTest {
    @Test
    @DisplayName("request method에서 파일 경로를 분리해야 한다.")
    void extractPath() {
        String requestLine = "GET /index.html HTTP/1.1\n";

        String requestURL = StringUtils.separatePath(requestLine);
        assertThat(requestURL).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("request method의 경로가 /registration일 경우 회원가입 페이지의 경로를 반환해야 한다.")
    void getRegisterPath() {
        String requestLine = "GET /registration HTTP/1.1\n";

        String requestURL = StringUtils.separatePath(requestLine);
        String registerLocation = StringUtils.makeCompletePath(requestURL);
        assertThat(registerLocation).isEqualTo("src/main/resources/static/registration/index.html");
    }
}
