package webserver.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.Map;

class HttpRequestParserTest {
    @Test
    @DisplayName("쿼리 파라미터를 Map으로 변환")
    void parseQueryParams() throws UnsupportedEncodingException {
        String query = "userId=cori123&password=1234&name=cori&email=cori@naver.com";
        Map<String, String> userForm = HttpRequestParser.parseKeyValuePairs(query);

        Map<String, String> expected = Map.of(
                "userId", "cori123",
                "password", "1234",
                "name", "cori",
                "email","cori@naver.com");
        Assertions.assertThat(userForm).isEqualTo(expected);
    }
}