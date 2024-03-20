package webserver;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestLine;
import request.RequestLineParser;
import response.RegistrationHandler;
import response.UserHandler;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MainHandlerTest {
    @Test
    @DisplayName("Request line 에서 requestMethod 를 추출하였습니다.")
    void getRequestMethodTest() throws IOException {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMethod = "GET";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getRequestMethod()).isEqualTo(expectedMethod);
    }
    @Test
    @DisplayName("Request line 에서 requestPath 를 추출하였습니다.")
    void getRequestPathTest() throws IOException {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedPath = "/index.html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getRequestPath()).isEqualTo(expectedPath);
    }
    @Test
    @DisplayName("Request line 에서 path 에 있는 file extension 을 통해 mimeType 를 반환하였습니다.")
    void getMimeTypeTest() throws IOException {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMimeType = "text/html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getMimeType()).isEqualTo(expectedMimeType);
    }
    @Test
    @DisplayName("회원가입 요청에서 원하는 유저 데이터를 추출하였습니다.")
    void getUserDataTest() throws IOException {
        String registrationRequest = "username=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        User user1 = UserHandler.createUserFromData(RegistrationHandler.extractUserData(registrationRequest));
        assertThat(user1.getUserId()).isEqualTo(new User("MirID","password","미르").getUserId());
    }
    @Test
    @DisplayName("이름이 한글인 경우 UTF-8로 변환해서 추출하였습니다.")
    void getUserDataKRTest() throws IOException {
        String registrationRequest = "username=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        User user1 = UserHandler.createUserFromData(RegistrationHandler.extractUserData(registrationRequest));
        assertThat(user1.getName()).isEqualTo(new User("MirID","password","미르").getName());
    }

}