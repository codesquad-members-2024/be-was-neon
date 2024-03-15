package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Request.Request;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestTest {

    @Test
    @DisplayName("getFile 요청에 대해 알맞은 Request 객체가 반환된다")
    void getFileRequest(){
        Request request = new Request("GET /index.html HTTP/1.1");

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("createUser 요청에 대해 매개변수가 알맞게 파싱되고 , Request 객체가 반환되어야 한다")
    void createUserRequest(){
        String url = "GET /create?userId=test&password=test&name=test&email=test%40naver.com HTTP/1.1";
        Request request = new Request(url);
        Map<String , String> params = request.getQuery();

        assertThat(params.get("userId")).isEqualTo("test");
        assertThat(params.get("password")).isEqualTo("test");
        assertThat(params.get("name")).isEqualTo("test");
        assertThat(params.get("email")).isEqualTo("test%40naver.com");
    }
}
