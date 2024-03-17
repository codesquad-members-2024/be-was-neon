package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @Test
    @DisplayName("Request line 에서 원하는 파일 이름을 추출하였습니다.")
    void getFileNameTest() throws IOException {
        String httpRequest = "GET /index.html HTTP/1.1\nHost: localhost:8080\n";
        String expectedPath = "/index.html";
        assertThat(HttpRequest.getURL(httpRequest)).isEqualTo(expectedPath);
    }
    @ParameterizedTest
    @CsvSource({"username, MirID", "nickname, 미르", "password, password"})
    @DisplayName("회원가입 요청에서 원하는 유저 데이터를 추출하였습니다.")
    void getUserDataTest(String key, String value) throws IOException {
        String registrationRequest = "/create?username=MirID&nickname=%EB%AF%B8%EB%A5%B4&password=password";
        Map<String, String> userData = RegistrationResponse.getUserData(registrationRequest);
        assertThat(userData.get(key)).isEqualTo(value);
    }
    @ParameterizedTest
    @MethodSource("headerData")
    @DisplayName("Request Header 에서 해당하는 키 값을 추출")
    void parseHeadersTest(String key, String value, String httpRequest) throws IOException {
        Map<String, String> httpData = HttpRequest.parseHeader(httpRequest);
        assertThat(httpData.get(key)).isEqualTo(value);
    }

    static Stream<Arguments> headerData() {
        return Stream.of(
                Arguments.arguments("Cookie", "Idea-4c21aba4=61fe7b21-51a0-4c27-bc1e-c5bc0385bf92", "Cookie: Idea-4c21aba4=61fe7b21-51a0-4c27-bc1e-c5bc0385bf92"),
                Arguments.arguments("Connection", "keep-alive", "Connection: keep-alive"),
                Arguments.arguments("Host", "localhost:8080", "Host: localhost:8080"),
                Arguments.arguments("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7", "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
        );
    }
}