package utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExtractorTest {
    private static final String BLANK = "\s+";

    @DisplayName("공백을 기준으로 문자열을 추출할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /index.html HTTP/1.1",
            "GET /reset.css HTTP/1.1",
            "GET /global.css HTTP/1.1",
            "GET /main.css HTTP/1.1",
            "GET /img/signiture.svg HTTP/1.1",
            "GET /img/like.svg HTTP/1.1",
            "GET /img/sendLink.svg HTTP/1.1",
            "GET /img/bookMark.svg HTTP/1.1",
            "GET /img/ci_chevron-left.svg HTTP/1.1",
            "GET /img/ci_chevron-right.svg HTTP/1.1",
            "GET /favicon.ico HTTP/1.1)"
    })
    void extract(String httpHeader) {
        assertThat(Extractor.extract(httpHeader)).isEqualTo(httpHeader.split(BLANK));
    }

    @DisplayName("Http Request Header에서 url을 추출할 수 있다.")
    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/1.1, /index.html",
            "GET /reset.css HTTP/1.1, /reset.css",
            "GET /global.css HTTP/1.1, /global.css",
            "GET /main.css HTTP/1.1, /main.css",
            "GET /img/signiture.svg HTTP/1.1, /img/signiture.svg",
            "GET /img/like.svg HTTP/1.1, /img/like.svg",
            "GET /img/sendLink.svg HTTP/1.1, /img/sendLink.svg",
            "GET /img/bookMark.svg HTTP/1.1, /img/bookMark.svg",
            "GET /img/ci_chevron-left.svg HTTP/1.1, /img/ci_chevron-left.svg",
            "GET /img/ci_chevron-right.svg HTTP/1.1, /img/ci_chevron-right.svg",
            "GET /favicon.ico HTTP/1.1, /favicon.ico"
    })
    void extractUrl(String httpHeader, String url) {
        assertThat(Extractor.extractUrl(httpHeader)).isEqualTo(url);
    }

    @DisplayName("Http Request에서 Http Method를 추출할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /index.html HTTP/1.1",
            "GET /reset.css HTTP/1.1",
            "GET /global.css HTTP/1.1",
            "GET /main.css HTTP/1.1",
            "GET /img/signiture.svg HTTP/1.1",
            "GET /img/like.svg HTTP/1.1",
            "GET /img/sendLink.svg HTTP/1.1",
            "GET /img/bookMark.svg HTTP/1.1",
            "GET /img/ci_chevron-left.svg HTTP/1.1",
            "GET /img/ci_chevron-right.svg HTTP/1.1",
            "GET /favicon.ico HTTP/1.1)"
    })
    void extractMethod(String httpHeader) {
        assertThat(Extractor.extractMethod(httpHeader)).isEqualTo("GET");
    }

    @DisplayName("user 생성 요청에서 user의 아이디, 비밀번호, 이름, 이메일을 추출할 수 있다.")
    @Test
    void extractUser() {
        String request = "/user/create?userId=javajigi&userPw=password&userName=%EB%B0%95%EC%9E%AC%EC%84%B1&userEmail=javajigi%40slipp.net";
        assertThat(Extractor.extractUser(request))
                .isEqualTo(new String[]{"javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net"});
    }
}