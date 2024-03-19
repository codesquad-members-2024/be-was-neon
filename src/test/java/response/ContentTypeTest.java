package response;

import org.junit.jupiter.api.*;
import request.FileInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentTypeTest {
    @Test
    @DisplayName("html 파일이면 ContentType으로 'text/html;charset=utf-8'을 반환 한다.")
    void getHtmlContentType() {
        String completePath = "src/main/resources/static/registration/index.html";
        assertThat(ContentType.getContentType(FileInfo.getFileType(completePath))).isEqualTo("text/html;charset=utf-8");
    }

    @Test
    @DisplayName("css 파일이면 ContentType으로 'text/css;charset=utf-8'을 반환 한다.")
    void getCssContentType() {
        String completePath = "src/main/resources/static/main.css";
        assertThat(ContentType.getContentType(FileInfo.getFileType(completePath))).isEqualTo("text/css;charset=utf-8");
    }

    @Test
    @DisplayName("svg 파일이면 ContentType으로 'image/svg+xml'을 반환 한다.")
    void getSvgContentType() {
        String completePath = "src/main/resources/static/img/signiture.svg";
        assertThat(ContentType.getContentType(FileInfo.getFileType(completePath))).isEqualTo("image/svg+xml");
    }
}
