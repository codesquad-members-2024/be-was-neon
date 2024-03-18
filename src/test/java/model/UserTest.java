package model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class UserTest {
    @Test
    @DisplayName("map의 value로 User 객체를 생성할 수 있다.")
    void createUser() {
        Map<String, String> userForm = Map.of(
                "userId", "cori123",
                "password", "1234",
                "name", "cori",
                "email","cori@naver.com");
        User actual = User.from(userForm);
        User expected = new User("cori123", "1234", "cori", "cori@naver.com");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}