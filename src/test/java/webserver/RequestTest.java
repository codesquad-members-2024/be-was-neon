package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RequestTest {

    @ParameterizedTest
    @CsvSource({"GET /index.html , GETFILE  :  /index.html"})
    @DisplayName("getFile 요청에 대해 알맞은 Request 객체가 반환된다")
    void getFileRequest(String url , String log) throws IOException {
        Request request = makeRequest(url);
        assertThat(request.getLog()).isEqualTo(log);
    }

    @ParameterizedTest
    @CsvSource({"GET /, GETFILE  :  //index.html" , "GET /registration , GETFILE  :  /registration/index.html"})
    @DisplayName("url 이 경로라면 해당 경로의 index.html 을 요청한 것으로 간주한다")
    void getPathRequest(String url , String log) throws IOException {
        Request request = makeRequest(url);
        assertThat(request.getLog()).isEqualTo(log);
    }

    @Test
    @DisplayName("createUser 요청에 대해 매개변수가 알맞게 파싱되고 , Request 객체가 반환되어야 한다")
    void createUserRequest() throws IOException {
        String url = "GET /create?userId=test&password=test&name=test&email=test%40naver.com";
        Request request = makeRequest(url);
        assertThat(request.getLog()).isEqualTo("CREATEUSER  :  /create?userId=test&password=test&name=test&email=test%40naver.com");
        assertThat(request.getParams().get(1)).isEqualTo("test");
    }

    private static Request makeRequest(String url) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(url.getBytes());
        return Request.makeRequest(inputStream);
    }
}
