package utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceHandlerTest {

    @DisplayName("src/main/resources/static/index.html 파일을 읽으면 바이트의 길이는 0을 초과한다")
    @Test
    void read_success() {
        // given
        String filePath = "src/main/resources/static/index.html";

        // when
        byte[] bytes = ResourceHandler.read(filePath);

        // then
        assertThat(bytes.length).isGreaterThan(0);
    }

    @DisplayName("존재하지 않는 파일을 읽으면 바이트의 길이는 0이다")
    @Test
    void read_fail() {
        // given
        String filePath = "not-found-path";

        // when
        byte[] bytes = ResourceHandler.read(filePath);

        // then
        assertThat(bytes.length).isEqualTo(0);
    }
}