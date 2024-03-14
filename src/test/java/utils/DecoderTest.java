package utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DecoderTest {
    @DisplayName("인코딩된 유저 정보를 디코딩할 수 있다.")
    @Test
    void decode() throws UnsupportedEncodingException {
        String[] userEncodedInfo = new String[]{"javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1",
                "javajigi%40slipp.net"};

        assertThat(Decoder.decodeUser(userEncodedInfo)).isEqualTo(new String[]{"javajigi", "password", "박재성", "javajigi@slipp.net"});
    }
}