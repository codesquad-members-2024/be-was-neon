package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.RequestStartLine;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class RequestTest {

    @Test
    @DisplayName("HTTP 요청에 대해 알맞은 Request 객체가 반환된다")
    void createRequest(){
        String startLine = "GET /index.html HTTP/1.1";
        Request request = new Request(startLine);
        RequestStartLine requestStartLine = request.getStartLine();

        assertSoftly(softly -> {
            softly.assertThat(requestStartLine.getMethod()).isEqualTo("GET");
            softly.assertThat(requestStartLine.getUri()).isEqualTo("/index.html");
            softly.assertThat(requestStartLine.getVersion()).isEqualTo("HTTP/1.1");
        });
    }

    @Test
    @DisplayName("url 에 담긴 query 가 알맞게 파싱되어 Map 형태로 저장되고 , 조회 가능하다")
    void createUserRequest(){
        String startLine = "GET /create?userId=test&password=test&name=test&email=test%40naver.com HTTP/1.1";
        Request request = new Request(startLine);

        assertSoftly(softly -> {
            softly.assertThat(request.getRequestQuery("userId")).isEqualTo("test");
            softly.assertThat(request.getRequestQuery("password")).isEqualTo("test");
            softly.assertThat(request.getRequestQuery("email")).isEqualTo("test%40naver.com");
            softly.assertThat(request.getRequestQuery("name")).isEqualTo("test");
        });
    }
}
