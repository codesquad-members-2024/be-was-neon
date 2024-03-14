package utils;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
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

    @DisplayName("확장자 .html, .css, .svg, .js를 포함하는 경로에서 확장자만 추출할 수 있다")
    @Test
    void getExtension() {
        // given
        List<String> uriList = List.of("/index.html", "/min.css", "/img/test.svg", "/main.js");

        // when
        List<String> extensions = uriList.stream()
                .map(ResourceHandler::getExtension)
                .toList();

        // then
        assertThat(extensions.size()).isEqualTo(4);
        assertThat(extensions).contains(
                ".html",
                ".css",
                ".svg",
                ".js"
        );
    }
}