package webserver.httpMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    HttpRequest httpRequest;

    @BeforeEach
    void init() {
        httpRequest = new HttpRequest(List.of("GET /create?userId=cori&password=1234&name=cori&email=cori@naver.com HTTP/1.1 \r\n"));
    }

    @Test
    void getUri() {
        assertThat(httpRequest.getUri()).isEqualTo("/create");
    }

    @Test
    void getQueryParams() {
        assertThat(httpRequest.getQueryParams()).isEqualTo("userId=cori&password=1234&name=cori&email=cori@naver.com");
    }



}