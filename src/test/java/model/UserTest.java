package model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    private static final String ID = "hobinchoi";
    private static final String PASSWORD = "codesquad";
    private static final String NAME = "최호빈";
    private static final String EMAIL = "hobin0116@naver.com";

    @DisplayName("User가 올바른 아이디, 비밀번호, 이름, 이메일 값으로 생성될 수 있다.")
    @Test
    void create() {
        User user = new User(ID, PASSWORD, NAME, EMAIL);
        assertThat(user.getUserId()).isEqualTo(ID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("User의 toString()메소드가 알맞은 문자열로 생성될 수 있다.")
    @Test
    void userToString() {
        User user = new User(ID, PASSWORD, NAME, EMAIL);
        String result = "User [userId=hobinchoi, password=codesquad, name=최호빈, email=hobin0116@naver.com]";
        assertThat(user.toString()).isEqualTo(result);
    }
}