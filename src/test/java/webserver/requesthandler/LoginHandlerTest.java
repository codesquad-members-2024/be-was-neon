package webserver.requesthandler;

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
import webserver.requesthandler.handlerimpl.LoginHandler;
import webserver.requesthandler.handlerimpl.RequestHandler;

class LoginHandlerTest {
    RequestHandler requestHandler;
    HttpRequest request;
    HttpResponse response;

    @BeforeEach
    void setUp() {
        requestHandler = new LoginHandler();
        request = new HttpRequest();
        response = new HttpResponse();

        User user = new User("sangchu", "123", "상추", "sangchu@gmail.com");
        Database.addUser(user);
    }

    @Test
    @DisplayName("저장된 유저 아이디와 비밀번호가 일치하면 로그인에 성공하고 세션ID가 저장된 쿠키 정보를 response에 담는다.")
    void login() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "sangchu");
        params.put("password", "123");
        request.setParams(params);

        requestHandler.handlePost(request, response);
        String cookieValue = response.getHeader("Set-Cookie");
        assertThat(cookieValue).isNotNull();
    }

    @Test
    @DisplayName("저장된 유저 아이디와 비밀번호가 일치하지 않으면 로그인에 실패한다.")
    void loginFail() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "sangchu");
        params.put("password", "125"); // 비밀번호 불일치
        request.setParams(params);

        requestHandler.handlePost(request, response);
        String cookieValue = response.getHeader("Set-Cookie");
        assertThat(cookieValue).isNull();
    }

    @AfterEach
    void tearDown() {
        Database.clear();
    }
}