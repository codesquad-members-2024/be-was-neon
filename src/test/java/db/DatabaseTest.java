package db;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DatabaseTest {

    @Test
    @DisplayName("로그인한 회원ID로 회원가입한 회원을 찾아낸다.")
    void findAll() {
        final User login = new User.Builder("종혁", "123")
                .build();
        final User register1 = new User.Builder("종혁", "123")
                .build();
        final User register2 = new User.Builder("그로밋", "1")
                .build();
        Database.addUser(register1);
        Database.addUser(register2);

        final String loginUserId = login.getUserId();
        final User found = Database.findUserById(loginUserId);

        Assertions.assertThat(found.isSame(register1)).isTrue();
    }
}