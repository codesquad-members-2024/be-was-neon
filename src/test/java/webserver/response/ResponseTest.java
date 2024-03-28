package webserver.response;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import response.HttpFileReader;
import response.ResponseLine;

import java.io.IOException;

import static enums.FilePath.STATIC_PATH;
import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest {
    @Test
    @DisplayName("Protocol version 과 Response 라인을 통해 responseLine 을 만들었습니다.")
    void responseLineTest(){
        ResponseLine responseLine = new ResponseLine("HTTP/1.1",200);
        assertThat(responseLine.toString()).isEqualTo("HTTP/1.1 200 OK \r\n");
    }

    @Test
    @DisplayName("SetBody 를 통해 html 파일의 이름 부분을 변경해주었습니다.")
    void responseBodyChangeTest() throws IOException {
        byte[] body1 = HttpFileReader.setBodyDefault(STATIC_PATH.getPath() + "/main/index.html");
        byte[] body2 = HttpFileReader.setBodyLoginSuccess(STATIC_PATH.getPath() + "/main/index.html", "name");
        assertThat(body1).isNotEqualTo(body2);
    }

    @Test
    @DisplayName("데이터베이스에 유저를 추가해 주었습니다.")
    void responseUserListTest() throws IOException {
        User user = new User("id","password","name");
        Database.addUser(user);
        byte[] body1 = HttpFileReader.setBodyDefault(STATIC_PATH.getPath() + "/user/index.html");
        byte[] body2 = HttpFileReader.setBodyUserList(STATIC_PATH.getPath() + "/user/index.html", Database.findAll());
        assertThat(body1).isNotEqualTo(body2);
    }
}
