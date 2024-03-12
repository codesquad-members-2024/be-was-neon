package utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DecoderTest {
    @DisplayName("인코딩된 문자열을 디코딩할 수 있다.")
    @Test
    void encode() throws UnsupportedEncodingException {
        String name = "%EB%B0%95%EC%9E%AC%EC%84%B1";
        String email = "javajigi%40slipp.net";

        assertThat(Decoder.decode(name)).isEqualTo("박재성");
        assertThat(Decoder.decode(email)).isEqualTo("javajigi@slipp.net");
    }
}