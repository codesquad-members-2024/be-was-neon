package db;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SessionStoreTest {

    @Test
    @DisplayName("쿠키와 유저 정보로 새 세션을 만들고 , 쿠키 정보로 로그인한 유저를 조회할 수 있다")
    void addSession() {
        User user = new User("test", "test", "test", "test@naver.com");
        SessionStore.addSession("1234", user , 10000);

        assertThat(SessionStore.getSession("1234")).isEqualTo(user);
    }

    @Test
    @DisplayName("세션은 지정한 시간 뒤에 만료되어 삭제된다")
    void expireSession() throws InterruptedException {
        User user = new User("test", "test", "test", "test@naver.com");
        SessionStore.addSession("1234", user , 1000);
        Thread.sleep(1100);

        assertThat(SessionStore.getSession("1234")).isEqualTo(null);
    }
}
