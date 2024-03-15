import Utils.HttpRequestParser;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

    @Test
    void testExtractPathValidRequest() {
        String httpRequest = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequestParser parser = new HttpRequestParser(httpRequest);
        String path = parser.extractPath();
        assertThat(path).isEqualTo("/"); // "isEqualTo"를 사용해 수정
    }

    @Test
    void testMakeCompletePathForRoot() {
        String httpRequest = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequestParser parser = new HttpRequestParser(httpRequest);
        String completePath = parser.makePath();
        assertThat(completePath).endsWith("/index.html"); // "endsWith" 사용은 유효
    }

    @Test
    void testMakeCompletePathForRegister() {
        String httpRequest = "GET /register HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequestParser parser = new HttpRequestParser(httpRequest);
        String completePath = parser.makePath();
        assertThat(completePath).endsWith("/registration/index.html");
    }

    @Test
    void testMakeCompletePathForLogin() {
        String httpRequest = "GET /login HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequestParser parser = new HttpRequestParser(httpRequest);
        String completePath = parser.makePath();
        assertThat(completePath).endsWith("/login/index.html");
    }

    @Test
    void testMakeCompletePathForNonStandardPath() {
        String httpRequest = "GET /some/other/path HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequestParser parser = new HttpRequestParser(httpRequest);
        String completePath = parser.makePath();
        assertThat(completePath).endsWith("/some/other/path.html");
    }
    // 필요에 따라 더 많은 테스트 케이스를 추가할 수 있습니다.
}
