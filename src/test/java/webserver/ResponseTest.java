package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


class ResponseTest {

    @Test
    @DisplayName("createUser 요청이 들어오면 유저가 DB에 추가되고 , 302 응답을 보내야 한다")
    void createUserResponse(){
        String startLine = "GET /create?userId=test&password=test&name=test&email=test%40naver.com HTTP/1.1";
    }

    @ParameterizedTest
    @CsvSource({"GET / HTTP/1.1", "GET /registration HTTP/1.1"})
    @DisplayName("url 이 경로라면 해당 경로의 index.html 을 요청한 것으로 간주한다")
    void getPathRequest(String startLine){
    }
}
