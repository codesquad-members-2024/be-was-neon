package webserver;

import db.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
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

        Response.sendResponse(dos,request);
        assertThat(Database.findUserById("test")).isNotNull();
    }

    private static Request makeRequest(String url) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(url.getBytes());
        return Request.makeRequest(inputStream);
    }
}
