package webserver.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.Map;

class ParserTest {
    @Test
    @DisplayName("쿼리 파라미터를 Map으로 변환")
    void parseQueryParams() throws UnsupportedEncodingException {
        String query = "userId=cori123&password=1234&name=cori&email=cori%40naver.com";
        Map<String, String> userForm = Parser.splitQuery(query);

        Map<String, String> expected = Map.of(
                "userId", "cori123",
                "password", "1234",
                "name", "cori",
                "email","cori@naver.com");
        Assertions.assertThat(userForm).isEqualTo(expected);
    }
}