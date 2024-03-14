package utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExtractorTest {
    private static final String BLANK = "\s+";
    private static final List<String> HTTP_REQUEST_HEADER = List.of(
            "GET /index.html HTTP/1.1",
            "GET /reset.css HTTP/1.1",
            "GET /global.css HTTP/1.1",
            "GET /main.css HTTP/1.1",
            "GET /img/signiture.svg HTTP/1.1",
            "GET /img/like.svg HTTP/1.1",
            "GET /img/sendLink.svg HTTP/1.1",
            "GET /img/bookMark.svg HTTP/1.1",
            "GET /img/ci_chevron-left.svg HTTP/1.1",
            "GET /img/ci_chevron-right.svg HTTP/1.1",
            "GET /favicon.ico HTTP/1.1);");
    private static final List<String> FILE_PATH = List.of(
           "/index.html",
           "/reset.css",
            "/global.css",
           "/main.css",
           "/img/signiture.svg",
            "/img/like.svg",
            "/img/sendLink.svg",
            "/img/bookMark.svg",
            "/img/ci_chevron-left.svg",
            "/img/ci_chevron-right.svg",
            "/favicon.ico");

    @DisplayName("공백을 기준으로 문자열을 추출할 수 있다.")
    @Test
    void extract() {
        HTTP_REQUEST_HEADER.forEach(header -> assertThat(Extractor.extract(header)).isEqualTo(header.split(BLANK)));
    }

    @DisplayName("Http Request에서 url을 추출할 수 있다.")
    @Test
    void extractUrl() {
        for (int index = 0; index < HTTP_REQUEST_HEADER.size(); index++) {
            String url = Extractor.extractUrl(HTTP_REQUEST_HEADER.get(index));
            assertThat(url).isEqualTo(FILE_PATH.get(index));
            String filePath = DirectoryMatcher.mathDirectory(url);
            File file = new File(filePath);
            assertThat(file.isFile()).isTrue();
            assertThat(file.length()).isGreaterThan(0);
        }
    }

    @DisplayName("Http Request에서 Http Method를 추출할 수 있다.")
    @Test
    void extractMethod() {
        HTTP_REQUEST_HEADER.forEach(header -> assertThat(Extractor.extractMethod(header)).isEqualTo("GET"));
    }

    @DisplayName("user 생성 요청에서 user의 아이디, 비밀번호, 이름, 이메일을 추출할 수 있다.")
    @Test
    void extractUser() {
        String request = "/user/create?userId=javajigi&userPw=password&userName=%EB%B0%95%EC%9E%AC%EC%84%B1&userEmail=javajigi%40slipp.net";
        assertThat(Extractor.extractUser(request))
                .isEqualTo(new String[]{"javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net"});
    }
}