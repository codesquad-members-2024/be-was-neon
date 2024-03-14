package webserver;

import db.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseTest {

    @Test
    @DisplayName("createUser 요청이 들어오면 유저가 DB에 추가되고 , 302 응답을 보내야 한다")
    void createUserResponse(){
        String url = "GET /create?userId=test&password=test&name=test&email=test%40naver.com";
        Request request = makeRequest(url);
        Response response = new Response(request);
        String header = new String(response.getHeader());

        assertThat(header.startsWith("HTTP/1.1 " + ResponseStatus.FOUND.getMessage())).isTrue();
        assertThat(Database.findUserById("test")).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"GET /", "GET /registration"})
    @DisplayName("url 이 경로라면 해당 경로의 index.html 을 요청한 것으로 간주한다")
    void getPathRequest(String url){
        Request request = makeRequest(url);
        Response response = new Response(request);
        String body = new String(response.getBody());
        System.out.println(body);

        assertThat(body.startsWith("<!DOCTYPE html>")).isTrue();
    }

    private static Request makeRequest(String url) {
        String[] splitUrl = url.split(" ");
        return new Request(splitUrl[0], splitUrl[1]);
    }
}
