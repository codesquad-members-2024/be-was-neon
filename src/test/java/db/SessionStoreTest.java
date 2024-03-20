package db;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SessionStoreTest {
    User user;
    @BeforeEach
    void createUser(){
        user = new User("test", "test", "test", "test@naver.com");
    }

    @Test
    @DisplayName("쿠키와 유저 정보로 새 세션을 만들고 , 쿠키 정보로 로그인한 유저를 조회할 수 있다")
    void addSession() {
        SessionStore.addSession("1234", user);

        assertThat(SessionStore.getSession("1234")).isEqualTo(user);
    }

    @Test
    @DisplayName("쿠키 정보로 세션을 삭제할 수 있다")
    void removeSession(){
        SessionStore.addSession("12345" , user);
        SessionStore.removeSession("12345");

        assertThat(SessionStore.getSession("12345")).isEqualTo(null);
    }
}
