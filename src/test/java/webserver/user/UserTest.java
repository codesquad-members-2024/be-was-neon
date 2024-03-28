package webserver.user;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import session.SessionManager;
import userservice.LoginHandler;
import userservice.UserDataParser;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    @DisplayName("회원가입 요청에서 원하는 유저 데이터를 추출하였습니다.")
    void getUserDataTest() throws IOException {
        String registrationRequest = "userId=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        Map<String, String> userData = UserDataParser.extractUserData(registrationRequest);
        assertThat(userData.get("userId")).isEqualTo("MirId");
    }

    @Test
    @DisplayName("이름이 한글인 경우 UTF-8로 변환해서 추출하였습니다.")
    void getUserDataKRTest() throws IOException {
        String registrationRequest = "userId=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        Map<String, String> userData = UserDataParser.extractUserData(registrationRequest);
        assertThat(userData.get("nickname")).isEqualTo("미르");
    }

    @Test
    @DisplayName("다른 유저가 로그인 할때마다 랜덤한 sessionId 생성 해주었습니다.")
    void generateSessionIdTest(){
        String sessionIdOne = SessionManager.generateSessionId();
        String sessionIdTwo = SessionManager.generateSessionId();
        assertThat(sessionIdOne).isNotEqualTo(sessionIdTwo);
    }

    @Test
    @DisplayName("로그인 하였을때 Database에 유저가 존재할 경우 True를 반환 해주었습니다.")
    void isLoginDataValidTest() throws IOException{
        LoginHandler loginHandler = new LoginHandler();
        Database.addUser(new User("MirId","password","미르"));
        String loginQueryParam = "userId=MirId&password=password";
        assertThat(loginHandler.isLoginDataValid(UserDataParser.extractUserData(loginQueryParam))).isEqualTo(Boolean.TRUE);
    }
}
