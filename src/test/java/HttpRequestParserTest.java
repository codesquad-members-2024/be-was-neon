import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import Utils.HttpRequestParser;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

class HttpRequestParserTest {

    private HttpRequestParser parser;

    @BeforeEach
    void setUp() {
        // 예제 HTTP 요청
        String httpRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n\r\n" +
                "Accept: */*";
        this.parser = new HttpRequestParser(httpRequest);
    }

    @Test
    void testExtractPath() {
        assertEquals("/login", parser.extractPath(), "경로 추출이 정확하지 않습니다.");
    }

    @Test
    void testMakePath() {
        String expectedPath = "src/main/resources/static/index.html";
        assertEquals(expectedPath, parser.makePath(), "파일 경로 생성이 정확하지 않습니다.");
    }

    @Test
    void testParseUserFromGetRequest() {
        // 이 경우 사용자 정보를 파싱할 수 없으므로 빈 Optional을 반환해야 합니다.
        assertTrue(parser.parseUserFromGetRequest().isEmpty(), "GET 요청에서 사용자 정보를 파싱하지 못해야 합니다.");
    }

    @Test
    void testParseUserFromGetRequestWithUserInfo() {
        // 사용자 정보가 포함된 GET 요청
        String httpRequestWithUser = "GET /create?userId=123&password=456&name=kalia&email=kalia@123456 HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n\r\n";
        HttpRequestParser parserWithUser = new HttpRequestParser(httpRequestWithUser);
        Optional<User> userOptional = parserWithUser.parseUserFromGetRequest();

        assertTrue(userOptional.isPresent(), "사용자 정보가 포함된 요청에서 사용자 객체가 생성되어야 합니다.");
        userOptional.ifPresent(user -> {
            assertEquals("123", user.getUserId(), "사용자 ID가 일치하지 않습니다.");
            assertEquals("456", user.getPassword(), "비밀번호가 일치하지 않습니다.");
            assertEquals("kalia", user.getName(), "이름이 일치하지 않습니다.");
            assertEquals("kalia@123456", user.getEmail(), "이메일이 일치하지 않습니다.");
        });
    }

    // 필요에 따라 추가적인 테스트 케이스 구현
}
