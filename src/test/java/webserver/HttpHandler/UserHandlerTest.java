package webserver.HttpHandler;

import db.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.Response;
import webserver.TestUtils;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UserHandlerTest {

    final UserHandler userHandler = new UserHandler();

    @AfterEach
    void clearDB(){
        Database.clear();
    }
    @Test
    @DisplayName("createUser 요청이 들어오면 유저가 DB에 추가되고 , 302 응답을 보낸다")
    void createUserTest() {
        Request request = TestUtils.createUserRequest;
        Response response = userHandler.createUser(request);

        assertSoftly(softly -> {
            softly.assertThat(response.getStartLine().toString()).isEqualTo("HTTP/1.1 302 Found");
            softly.assertThat(Database.findUserById("test").getName()).isEqualTo("test");
        });
    }

    @Test
    @DisplayName("이미 존재하는 유저 ID 라면 DB에 추가하지 않는다")
    void FailToCreateUser() {
        Request request = TestUtils.createUserRequest;
        userHandler.createUser(request);
        int beforeSize = Database.findAll().size();

        userHandler.createUser(request);

        assertThat(Database.findAll().size()).isEqualTo(beforeSize);
    }

}