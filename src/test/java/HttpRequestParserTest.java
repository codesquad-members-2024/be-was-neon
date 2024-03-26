import Utils.HttpRequestParser;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

    private HttpRequestParser parserWithUser;

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        // 사용자 정보가 포함된 POST 요청 바디를 시뮬레이션
        String httpRequestWithUser = "POST /create HTTP/1.1\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 57\r\n" +
                "\r\n" +
                "userId=123&password=123&name=123&email=123@123";
        parserWithUser = new HttpRequestParser(httpRequestWithUser);
    }

    @Test
    void testParseUserFromBodyWithUserInfo() {
        Optional<User> userOptional = parserWithUser.parseUserFromBody();

        assertThat(userOptional).isPresent().withFailMessage("사용자 정보가 포함된 요청에서 사용자 객체가 생성되어야 합니다.");
        userOptional.ifPresent(user -> {
            assertThat(user.getUserId()).isEqualTo("123").withFailMessage("사용자 ID가 일치하지 않습니다.");
            assertThat(user.getPassword()).isEqualTo("123").withFailMessage("비밀번호가 일치하지 않습니다.");
            assertThat(user.getName()).isEqualTo("123").withFailMessage("이름이 일치하지 않습니다.");
            assertThat(user.getEmail()).isEqualTo("123@123").withFailMessage("이메일이 일치하지 않습니다.");
        });
    }
}
