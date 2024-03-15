package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    void setHttpRequest() {
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
    @DisplayName("static 폴더에 있는 파일의 경로를 반환해야 한다.")
    void getRegisterPath() {
        String completePath = httpRequest.getCompletePath();
        assertThat(completePath).isEqualTo("src/main/resources/static/registration/index.html");
    }

    @Test
    @DisplayName("StartLine에서 RequestParameter를 추출 한다.")
    void parseRegisterInfo() {
        String requestStartLine = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        httpRequest = new HttpRequest(requestStartLine);
        httpRequest.parseRegisterData();

        assertThat(httpRequest.getUserId()).isEqualTo("javajigi");
        assertThat(httpRequest.getPassword()).isEqualTo("password");
        assertThat(httpRequest.getName()).isEqualTo("박재성");
        assertThat(httpRequest.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName("StartLine에서 RequestParameter를 추출한 후, user 객체를 DB에 저장해야 한다.")
    void storeUserAtDb() {
        String requestStartLine = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        httpRequest = new HttpRequest(requestStartLine);
        httpRequest.parseRegisterData();
        httpRequest.storeDatabase();

        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("박재성");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
    }
}