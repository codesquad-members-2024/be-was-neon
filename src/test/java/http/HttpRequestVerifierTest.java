package http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestVerifierTest {
    @DisplayName("올바른 HTTP request인지 확인한다.")
    @Test
    void verify() {
        String[] correctRequest = new String[]{"GET", "/registration/register.html", "HTTP/1.1"};
        assertThat(HttpRequestVerifier.verify(correctRequest)).isTrue();

        String[] invalidRequest = new String[]{"CODE_SQUAD", "/registration/register.html", "HTTP/0.0"};
        assertThat(HttpRequestVerifier.verify(invalidRequest)).isFalse();
    }

    @DisplayName("인자로 받은 경로에 있는 파일이 내용물이 존재하는 파일인지 검증한다.")
    @Test
    void verifyFile() {
        assertThat(HttpRequestVerifier.verifyFile("src/main/resources/static/index.html")).isTrue();
        assertThat(HttpRequestVerifier.verifyFile("src/main/resources/static/index/")).isFalse();
    }
}