package webserver;

import model.User;
import db.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestHandlerTest {

    @Test
    @DisplayName("Request Header에서 Request Parameter 를 추출하여 User 클래스에 담아야 한다.")
    void parseRegisterInfo() {
        String requestStartLine = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";

        RegisterRequestHandler registerRH = new RegisterRequestHandler(requestStartLine);
        User user = Database.findUserById("javajigi");
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("박재성");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
    }

}
