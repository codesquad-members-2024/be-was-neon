package webserver.HttpMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.WebServer;
import webserver.HttpMessage.constants.eums.FileType;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class MessageBodyTest {

    @DisplayName("body에 담긴 정보가 저장될때 Content-Type , Length 가 알맞게 설정되고 , 조회 가능하다")
    @Test
    void makeBody() {
        // given
        String requestBody = "userID=test&password=1234";

        // when
        MessageBody body = new MessageBody(requestBody, FileType.URLENCODED);

        //then
        assertSoftly(softly -> {
            assertThat(body.getContentType()).isEqualTo(FileType.URLENCODED);
            assertThat(body.getContentLength()).isEqualTo(requestBody.length());
        });
    }

    @DisplayName("Key - Value 형태의 Content-Type 이라면 파싱해 Map 에 저장하고 , 조회 가능하다")
    @Test
    void makeKeyValueBody() {
        // given
        String requestBody = "userID=test&password=1234";

        // when
        MessageBody body = new MessageBody(requestBody, FileType.URLENCODED);

        //then
        assertSoftly(softly -> {
            assertThat(body.getContentByKey("userID")).isEqualTo("test");
            assertThat(body.getContentByKey("password")).isEqualTo("1234");
        });
    }

    @DisplayName("body 의 내용을 file 로 설정할 수 있다")
    @Test
    void makeFileBody() throws IOException {
        // given
        File file = new File(WebServer.staticSourcePath + "/index.html");

        // when
        MessageBody body = new MessageBody(file);

        //then
        assertSoftly(softly -> {
            assertThat(body.getContentType()).isEqualTo(FileType.HTML);
            assertThat(body.getContentLength()).isEqualTo(file.length());
        });
    }
}
