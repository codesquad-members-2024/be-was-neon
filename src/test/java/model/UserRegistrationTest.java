package model;

import static org.assertj.core.api.Assertions.assertThat;

import db.Database;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegistrationTest {

    @Test
    @DisplayName("UserRegistration 클래스의 registration 메소드를 통해 Database에 제대로 들어가는지 확인")
    void registrationTest() throws UnsupportedEncodingException {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("id", "1");
        testMap.put("nickname", "lsh");
        testMap.put("password", "1234");
        testMap.put("email", "tmdgus717@naver.com");

        UserRegistration.register(testMap);
        User user = Database.findUserById("1");
        System.out.println(user);
        assertThat(user.getUserId()).isEqualTo("1");
        assertThat(user.getName()).isEqualTo("lsh");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getEmail()).isEqualTo("tmdgus717@naver.com");
    }
}
