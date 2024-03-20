package webserver.requesthandler.http;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {
    HttpResponse response;

    @BeforeEach
    void setUp() {
        response = new HttpResponse();
    }

    @Test
    @DisplayName("리다이렉트를 설정하면 302코드와 경로를 설정할 수 있다.")
    void setRedirect() {
        String path = "/";
        response.setRedirect(path);
        HttpStatus status = response.getStatus();
        String location = response.getHeader("Location");

        assertThat(status).isEqualTo(HttpStatus.FOUND);
        assertThat(location).isEqualTo(path);
    }

    @Test
    @DisplayName("바디를 입력하면 헤더 정보에 타입과 길이가 자동으로 설정된다.")
    void setBody() {
        int fileLength = 10;
        byte[] body = new byte[fileLength];
        response.setBody(body, ContentType.PNG);
        String contentType = response.getHeader("Content-Type");
        String contentLength = response.getHeader("Content-Length");

        assertThat(contentType).isEqualTo(ContentType.PNG.getMimeType());
        assertThat(contentLength).isEqualTo(String.valueOf(fileLength));
    }
}