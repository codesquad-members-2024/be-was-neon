package utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParameterParserTest {

    @Test
    @DisplayName("사용자 정보를 담은 문자열 파싱 후 parameter 추출 기능 검증")
    void getUserInfo() {
        String input = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        Map<String, String> userInfo = ParameterParser.getUserParams(input);

        assertThat(userInfo.get("userId")).isEqualTo("javajigi");
        assertThat(userInfo.get("password")).isEqualTo("password");
        assertThat(userInfo.get("name")).isEqualTo("%EB%B0%95%EC%9E%AC%EC%84%B1");
        assertThat(userInfo.get("email")).isEqualTo("javajigi%40slipp.net");
    }
}