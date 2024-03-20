package webserver.requesthandler.handlerimpl;

import static org.assertj.core.api.Assertions.assertThat;

import db.Database;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;
import webserver.requesthandler.session.SessionManager;

class LogoutHandlerTest {

    RequestHandler requestHandler;
    HttpRequest request;
    HttpResponse response;

    @BeforeEach
    void setUp() {
        requestHandler = new LogoutHandler();
        request = new HttpRequest();
        response = new HttpResponse();

        User user = new User("sangchu", "123", "상추", "sangchu@gmail.com");
        SessionManager.createSessionBySID(user, response, "123456");
    }

    @Test
    @DisplayName("로그아웃 하면 세션 매니저가 가지고 있던 세션 정보를 만료시킨다.")
    void logout() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SID=123456");
        request.setHeaders(headers);

        Object session1 = SessionManager.findSession(request);
        assertThat(session1).isNotNull();

        requestHandler.handleGet(request, response); // 로그아웃 실행

        Object session2 = SessionManager.findSession(request);
        assertThat(session2).isNull();
    }

    @AfterEach
    void tearDown() {
        Database.clear();
    }
}
