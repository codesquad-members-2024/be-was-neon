package webserver.session;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.session.SessionManager;

class SessionManagerTest {

    HttpRequest request;
    HttpResponse response;

    @BeforeEach
    void setUp() throws IOException {
        String requestString = """
                GET /registration HTTP/1.1
                Cookie: SID=123456
                                
                """;

        byte[] byteArray = requestString.getBytes(StandardCharsets.UTF_8);

        InputStream in = new ByteArrayInputStream(byteArray);
        request = new HttpRequest(in);
        response = new HttpResponse();
    }

    @Test
    @DisplayName("세션매니저는 request가 쿠키로 가지고 있는 SID정보와 일치하는 User를 찾을 수 있다.")
    void findSession() {
        User user = new User("sangchu", "123", "상추", "sangchu@gmail.com");
        SessionManager.createSessionBySID(user, response, "123456"); // 실제 로직은 sid를 직접 입력하지 않고 임의 UUID를 생성한다.

        Object session = SessionManager.findSession(request);
        assertThat(session).isEqualTo(user);
    }

    @Test
    @DisplayName("request가 가지고 있는 쿠키정보와 일치하는 세션을 만료시킬 수 있다.")
    void expire() {
        User user = new User("sangchu", "123", "상추", "sangchu@gmail.com");
        SessionManager.createSessionBySID(user, response, "123456"); // 실제 로직은 sid를 직접 입력하지 않고 임의 UUID를 생성한다.
        assertThat(SessionManager.findSession(request)).isNotNull();

        SessionManager.expire(request); // 세션을 만료시킨다.

        Object session = SessionManager.findSession(request);
        assertThat(session).isNull();
    }
}