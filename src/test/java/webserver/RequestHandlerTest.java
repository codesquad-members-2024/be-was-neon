package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utils.IOUtils;
import utils.RegistrationResponse;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    @DisplayName("Request line 에서 원하는 파일 이름을 추출하였습니다.")
    void getFileNameTest() throws IOException {
        String httpRequest = "GET /index.html HTTP/1.1\nHost: localhost:8080\n";
        String expectedPath = "/index.html";
        assertThat(IOUtils.getFileName(httpRequest)).isEqualTo(expectedPath);
    }
    @ParameterizedTest
    @CsvSource({"username, MirID", "nickname, 미르", "password, password"})
    @DisplayName("회원가입 요청에서 원하는 유저 데이터를 추출하였습니다.")
    void getUserDataTest(String key, String value) throws IOException {
        String registrationRequest = "/create?username=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        Map<String, String> userData = RegistrationResponse.getUserData(registrationRequest);
        assertThat(userData.get(key)).isEqualTo(value);
    }

}