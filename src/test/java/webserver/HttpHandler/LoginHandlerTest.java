package webserver.HttpHandler;

import db.Database;
import db.SessionStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.Response;
import webserver.Mapping.MappingMatcher;
import webserver.TestUtils;
import webserver.eums.FileType;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class LoginHandlerTest {

    final LoginHandler loginHandler = new LoginHandler();

    @BeforeEach
    void clearStore(){
        Database.clear();
        SessionStore.clear();
        System.out.println("clear");
    }

    @DisplayName("/login 요청의 유저 ID 비밀번호가 DB와 일치하면 로그인에 성공하고 , 302 리다이렉션 응답을 보낸다")
    @Test
    void login() throws Exception {
        // given
        MappingMatcher matcher = new MappingMatcher(TestUtils.createUserRequest);
        matcher.getResponse();

        // when
        Response response = loginHandler.login(new Request("POST /login HTTP/1.1")
            .body(new MessageBody("userId=test&password=test" , FileType.URLENCODED)));

        //then
        assertSoftly(softly -> {
            softly.assertThat(SessionStore.getSize()).isEqualTo(1);
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
        });
    }

    @DisplayName("/login 요청의 유저 ID나 비밀번호가 DB와 일치하지 않으면 로그인에 실패한다")
    @Test
    void loginFail() throws Exception {
        // given
        MappingMatcher matcher = new MappingMatcher(TestUtils.createUserRequest);
        matcher.getResponse();


        // when
        Response response = loginHandler.login(new Request("POST /login HTTP/1.1")
                .body(new MessageBody("userId=test&password=wrong", FileType.URLENCODED)));

        //then
        assertSoftly(softly -> {
            softly.assertThat(SessionStore.getSize()).isEqualTo(0);
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
        });
    }

}