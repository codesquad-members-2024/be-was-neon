package webserver.http;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.requesthandler.http.ContentType;

class ContentTypeTest {

    @Test
    @DisplayName("파일 확장자에 따른 content-type 을 맵핑할 수 있다.")
    void getContentType() {
        ContentType defaultType = ContentType.getContentTypeByExtension("");
        assertThat(defaultType).isEqualTo(ContentType.HTML);

        ContentType jpeg = ContentType.getContentTypeByExtension("jpeg");
        assertThat(jpeg).isEqualTo(ContentType.JPEG);
    }
}