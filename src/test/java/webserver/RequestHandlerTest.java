package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {
    @Test
    @DisplayName("Request line 에서 원하는 파일 이름을 추출하였습니다.")
    void getFileNameTest() throws IOException {
        String httpRequest = "GET /index.html HTTP/1.1\nHost: localhost:8080\n";
        String expectedPath = "/index.html";

        RequestHandler requestHandler = new RequestHandler(null);
        assertThat(requestHandler.getFileName(httpRequest)).isEqualTo(expectedPath);
    }
}