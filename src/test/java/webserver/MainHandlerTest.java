package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestLine;
import request.RequestLineParser;
import session.LoginHandler;
import session.SessionManager;
import userservice.UserHandler;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MainHandlerTest {
    @Test
    @DisplayName("Request line 에서 requestMethod 를 추출하였습니다.")
    void getRequestMethodTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMethod = "GET";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getMethod()).isEqualTo(expectedMethod);
    }
    @Test
    @DisplayName("Request line 에서 requestPath 를 추출하였습니다.")
    void getRequestPathTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedPath = "/index.html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getPath()).isEqualTo(expectedPath);
    }
    @Test
    @DisplayName("Request line 에서 path 에 있는 file extension 을 통해 mimeType 를 반환하였습니다.")
    void getMimeTypeTest() {
        String givenRequestLine = "GET /index.html HTTP/1.1";
        String expectedMimeType = "text/html";
        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(givenRequestLine);
        assertThat(requestLine.getMimeType()).isEqualTo(expectedMimeType);
    }
    @Test
    @DisplayName("회원가입 요청에서 원하는 유저 데이터를 추출하였습니다.")
    void getUserDataTest() throws IOException {
        String registrationRequest = "userId=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        User user = UserHandler.createUserFromData(UserHandler.extractUserData(registrationRequest));
        assertThat(user.getUserId()).isEqualTo(new User("MirID","password","미르").getUserId());
    }
    @Test
    @DisplayName("이름이 한글인 경우 UTF-8로 변환해서 추출하였습니다.")
    void getUserDataKRTest() throws IOException {
        String registrationRequest = "userId=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        User user = UserHandler.createUserFromData(UserHandler.extractUserData(registrationRequest));
        assertThat(user.getName()).isEqualTo(new User("MirID","password","미르").getName());
    }
    @Test
    @DisplayName("다른 유저가 로그인 할때마다 랜덤한 sessionId 생성 해주었습니다.")
    void generateSessionIdTest(){
        String sessionIdOne = SessionManager.generateSessionId();
        String sessionIdTwo = SessionManager.generateSessionId();
        assertThat(sessionIdOne).isNotEqualTo(sessionIdTwo);
    }
    @Test
    @DisplayName("Request GET으로 로그인 정보를 가져왔을때 QueryParameter 부분을 추출 해주었습니다.")
    void getQueryParamTest() {
        RequestLineParser requestLineParser = new RequestLineParser();
        String loginRequest = "GET /user?userId=MirId&password=password HTTP/1.1";
        String expectedQueryParam = "userId=MirId&password=password";
        RequestLine requestLine = requestLineParser.parse(loginRequest);
        assertThat(requestLine.getQueryParam()).isEqualTo(expectedQueryParam);
    }
    @Test
    @DisplayName("로그인 하였을때 Database에 유저가 존재할 경우 True를 반환 해주었습니다.")
    void isLoginDataValidTest() throws IOException{
        Database.addUser(new User("MirId","password","미르"));
        String loginQueryParam = "userId=MirId&password=password";
        assertThat(LoginHandler.isLoginDataValid(loginQueryParam)).isEqualTo(Boolean.TRUE);
    }

}