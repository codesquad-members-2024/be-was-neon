package utils;

import static org.assertj.core.api.Assertions.assertThat;

import utils.FileExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileExtractorTest {
    private static final String HTTP_REQUEST_HEADER =
            """
            GET /index.html HTTP/1.1
            GET /reset.css HTTP/1.1
            GET /global.css HTTP/1.1
            GET /main.css HTTP/1.1
            GET /img/signiture.svg HTTP/1.1
            GET /img/like.svg HTTP/1.1
            GET /img/sendLink.svg HTTP/1.1
            GET /img/bookMark.svg HTTP/1.1
            GET /img/ci_chevron-left.svg HTTP/1.1
            GET /img/ci_chevron-right.svg HTTP/1.1
            GET /favicon.ico HTTP/1.1
            """;

    private static final String FILE_PATH =
            """
            /index.html
            /reset.css
            /global.css
            /main.css
            /img/signiture.svg
            /img/like.svg
            /img/sendLink.svg
            /img/bookMark.svg
            /img/ci_chevron-left.svg
            /img/ci_chevron-right.svg
            /favicon.ico
            """;

    @DisplayName("path에 해당하는 파일을 추출할 수 있다.")
    @Test
    void extractUrl() {
        String[] requestLines = HTTP_REQUEST_HEADER.split("\n");
        String[] filePaths = FILE_PATH.split("\n");

        for (int index = 0; index < requestLines.length; index++) {
            assertThat(FileExtractor.extractUrl(requestLines[index])).isEqualTo(filePaths[index]);
        }
    }
}