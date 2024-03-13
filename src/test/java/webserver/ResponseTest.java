package webserver;

import db.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResponseTest {
    List<Byte> dataOutput;

    DataOutputStream dos = new DataOutputStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
    });

    @Test
    @DisplayName("createUser 요청에 대해 매개변수가 알맞게 파싱되고 , Request 객체가 반환되어야 한다")
    void createUserResponse() throws IOException {
        String url = "GET /create?userId=test&password=test&name=test&email=test%40naver.com";
        Request request = makeRequest(url);

        Response.sendResponse(dos, request);
        assertThat(Database.findUserById("test")).isNotNull();
    }

    private static Request makeRequest(String url) {
        String[] splitUrl = url.split(" ");
        return new Request(splitUrl[0], splitUrl[1]);
    }
}
