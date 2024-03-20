package webserver.authenticator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import webserver.requesthandler.authenticator.Authenticator;
import webserver.requesthandler.authenticator.UnauthenticatedURLs;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;
import webserver.requesthandler.http.HttpStatus;
import webserver.requesthandler.session.SessionManager;

class AuthenticatorTest {
    Authenticator authenticator;
    HttpRequest request;
    HttpResponse response;

    @BeforeEach
    void setUp() {
        authenticator = new Authenticator(new UnauthenticatedURLs());
        request = new HttpRequest();
        response = new HttpResponse(new ByteArrayOutputStream());
    }

    @ParameterizedTest
    @DisplayName("인증이 필요한 경로에 미로그인 상태로 접근하면 로그인 화면으로 리다이렉트 되며 원래 접근하려고 했던 경로를 쿼리 파람으로 전달한다.")
    @ValueSource(strings = {"/article", "/comment"})
    void isUnAuthenticated(String path) throws IOException {
        request.setPath(path);
        request.setHeaders(new HashMap<>());
        boolean isAuthenticated = authenticator.isAuthenticated(request, response);
        String redirectURL = response.getHeader("Location");

        assertThat(isAuthenticated).isFalse(); // 미로그인 상태이므로 false
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(redirectURL).isEqualTo("/login?redirectURL=" + path);
    }

    @ParameterizedTest
    @DisplayName("인증이 필요한 경로에 로그인 상태로 접근하면 정상 응답이 나온다.")
    @ValueSource(strings = {"/article", "/comment"})
    void isAuthenticated(String path) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SID=123456");
        request.setPath(path);
        request.setHeaders(headers);

        SessionManager.createSessionBySID(new User("sangchu", "123", "상추", "didi1484@gmail.com"), response, "123456");

        boolean isAuthenticated = authenticator.isAuthenticated(request, response);
        String redirectURL = response.getHeader("Location");

        assertThat(isAuthenticated).isTrue(); // 로그인 상태이므로 True
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(redirectURL).isNull();
    }

    @ParameterizedTest
    @DisplayName("인증이 불필요한 경로에는 언제든지 접근할 수 있다.")
    @ValueSource(strings = {"/", "/css/abc.css", "/img/img.png", "/login", "/registration"})
    void isAccessible(String path) throws IOException {
        Map<String, String> headers = new HashMap<>();
        request.setPath(path);
        request.setHeaders(headers);

        boolean isAuthenticated = authenticator.isAuthenticated(request, response);
        assertThat(isAuthenticated).isTrue();
    }

}