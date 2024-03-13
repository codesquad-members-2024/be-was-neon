package model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("id=test, password=1234, name=testUser, email=test@test.org로 유저 객체를 생성할 수 있다")
    @Test
    void createUser() {
        // given
        String id = "test";
        String password = "1234";
        String username = "testUser";
        String email = "test@test.org";

        // when
        User user = new User(id, password, username, email);

        // then
        assertThat(user).extracting("userId").isEqualTo("test");
        assertThat(user).extracting("password").isEqualTo("1234");
        assertThat(user).extracting("name").isEqualTo("testUser");
        assertThat(user).extracting("email").isEqualTo("test@test.org");
    }
}