package utils;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*; // AssertJ

public class StringUtilsTest {
    @Test
    @DisplayName("request method에서 파일 경로를 분리해야 한다.")
    void extractPath() {
        String requestLine = "GET /index.html HTTP/1.1\n";

        String requestURL = StringUtils.separatePath(requestLine);
        assertThat(requestURL).isEqualTo("/index.html");
    }




}
