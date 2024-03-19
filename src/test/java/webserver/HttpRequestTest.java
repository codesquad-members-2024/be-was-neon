package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.*;
import request.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    void setHttpRequest() {
        httpRequest = new HttpRequest();
    }

    @Test
    @DisplayName("StartLine에서 method를 가져와야 한다.")
    void getMethod(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("StartLine에서 url을 가져와야 한다.")
    void getUrl(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getUrl()).isEqualTo("/registration");
    }

    @Test
    @DisplayName("StartLine에서 version을 가져와야 한다.")
    void getVersion(){
        httpRequest.storeStartLineData("GET /registration HTTP/1.1");
        assertThat(httpRequest.getVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("POST 방식으로 전달된 회원가입 정보를 파싱할 수 있어야 한다.")
    void parsePostBodyData() {
        httpRequest.storeStartLineData("POST /user/create HTTP/1.1");
        httpRequest.storeHeadersData("Content-Length: 59"); // headers 중에 필요한 정보만 전달
        httpRequest.storeBodyData("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");

        assertThat(httpRequest.getUserId()).isEqualTo("javajigi");
        assertThat(httpRequest.getPassword()).isEqualTo("password");
        assertThat(httpRequest.getName()).isEqualTo("박재성");
        assertThat(httpRequest.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName("파싱한 회원가입 정보를 user 객체에 담은 후 DB에 저장해야 한다.")
    void storeUserAtDb() {
        httpRequest.storeStartLineData("POST /user/create HTTP/1.1");
        httpRequest.storeHeadersData("Content-Length: 59"); // headers 중에 필요한 정보만 전달
        httpRequest.storeBodyData("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        httpRequest.storeDatabase(httpRequest.createUser()); // User 객체 생성 후 DB에 저장

        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("박재성");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
    }

}