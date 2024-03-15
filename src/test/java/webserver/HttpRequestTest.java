package webserver;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest("GET /registration HTTP/1.1");
    }

    @Test
    @DisplayName("StartLine에서 method를 가져와야 한다.")
    void getMethod(){
        assertThat(httpRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("StartLine에서 url을 가져와야 한다.")
    void getUrl(){
        assertThat(httpRequest.getUrl()).isEqualTo("/registration");
    }

    @Test
    @DisplayName("StartLine에서 version을 가져와야 한다.")
    void getVersion(){
        assertThat(httpRequest.getVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("static 폴더에 있는 파일의 경로를 반환해야 한다")
    void getRegisterPath() {
        String completePath = httpRequest.getCompletePath();
        assertThat(completePath).isEqualTo("src/main/resources/static/registration/index.html");
    }
}