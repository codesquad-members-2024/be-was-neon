package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestTest {

    @ParameterizedTest
    @CsvSource({"GET /index.html , GET : /index.html"})
    @DisplayName("getFile 요청에 대해 알맞은 Request 객체가 반환된다")
    void getFileRequest(String url , String log) throws IOException {
        Request request = makeRequest(url);
        assertThat(request.getLog()).isEqualTo(log);
    }

    @Test
    @DisplayName("createUser 요청에 대해 매개변수가 알맞게 파싱되고 , Request 객체가 반환되어야 한다")
    void createUserRequest(){
        String url = "GET /create?userId=test&password=test&name=test&email=test%40naver.com";
        Request request = makeRequest(url);
        assertThat(request.getParams().get("userId")).isEqualTo("test");
        assertThat(request.getParams().get("password")).isEqualTo("test");
        assertThat(request.getParams().get("name")).isEqualTo("test");
        assertThat(request.getParams().get("email")).isEqualTo("test%40naver.com");

    }

    private static Request makeRequest(String url) {
        String[] splitUrl = url.split(" ");
        return new Request(splitUrl[0] , splitUrl[1]);
    }
}
