package webserver;

import db.Database;
import db.SessionStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.HttpHandler.RequestHandler;
import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.MessageHeader;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.Response;
import webserver.eums.FileType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RequestHandlerTest {
    private static final RequestHandler requestHandler = new RequestHandler();

    @ParameterizedTest
    @CsvSource({"GET /index.html HTTP/1.1", "GET /registration/index.html HTTP/1.1"})
    @DisplayName("요청 url path로 특정할 수 있는 리소스를 응답한다")
    void getResourceRequest(String startLine) {

        Request request = new Request(startLine);
        Response response = requestHandler.responseGet(request);
        String body = new String(response.getBody());
        Map<String, String> headerFields = response.getHeader().getHeaderFields();

        assertThat(headerFields.get("Content-Type")).isEqualTo("text/html; charset=utf-8");
        assertThat(body.startsWith("<!DOCTYPE html>")).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"GET / HTTP/1.1", "GET /registration HTTP/1.1"})
    @DisplayName("url path로 특정할 수 있는 리소스가 없다면 , 해당 경로의 index.html 을 요청한 것으로 간주한다")
    void getPathRequest(String startLine) {
        Request request = new Request(startLine);
        Response response = requestHandler.responseGet(request);
        String body = new String(response.getBody());
        Map<String, String> headerFields = response.getHeader().getHeaderFields();

        assertThat(headerFields.get("Content-Type")).isEqualTo("text/html; charset=utf-8");
        assertThat(body.startsWith("<!DOCTYPE html>")).isTrue();
    }

    @Test
    @DisplayName("createUser 요청이 들어오면 유저가 DB에 추가되고 , 302 응답을 보낸다")
    void createUserTest() {
        Request request = TestUtils.createUserRequest;
        Response response = requestHandler.createUser(request);

        assertSoftly(softly -> {
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
            softly.assertThat(Database.findUserById("test").getName()).isEqualTo("test");
        });
    }

    @Test
    @DisplayName("이미 존재하는 유저 ID 라면 DB에 추가하지 않는다")
    void FailToCreateUser() {
        Request request = TestUtils.createUserRequest;
        requestHandler.createUser(request);
        int beforeSize = Database.findAll().size();

        requestHandler.createUser(request);

        assertThat(Database.findAll().size()).isEqualTo(beforeSize);
    }

    @DisplayName("/login 요청의 유저 ID 비밀번호가 DB와 일치하면 로그인에 성공하고 , 302 리다이렉션 응답을 보낸다")
    @Test
    void login() {
        // given
        requestHandler.createUser(TestUtils.createUserRequest);

        // when
        Response response = requestHandler.login(TestUtils.loginRequest);

        //then
        assertSoftly(softly -> {
            softly.assertThat(SessionStore.getSize()).isEqualTo(1);
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
        });
    }

    @DisplayName("/login 요청의 유저 ID나 비밀번호가 DB와 일치하지 않으면 로그인에 실패한다")
    @Test
    void loginFail() {
        // given
        requestHandler.createUser(TestUtils.createUserRequest);

        // when
        Response response = requestHandler.login(TestUtils.loginRequest
                .body(new MessageBody("userId=test&password=wrong", FileType.URLENCODED)));

        //then
        assertSoftly(softly -> {
            softly.assertThat(SessionStore.getSize()).isEqualTo(0);
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
        });
    }
}
