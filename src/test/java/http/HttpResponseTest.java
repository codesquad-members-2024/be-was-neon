package http;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @DisplayName("HttpResponse 객체를 생성할 수 있다")
    @Test
    void createHttpResponse() {
        // given
        HttpResponse response = new HttpResponse(new DataOutputStream(byteArrayOutputStream));

        // when
        response.setHttpVersion("HTTP/1.1")
                .setStatusCode(HttpStatus.STATUS_OK)
                .setContentType("text/html")
                .setCharset("utf-8")
                .setContentLength(100)
                .setLocation("/myLocation.html")
                .setSetCookie("myCookie=myValue; Path=/index.html;")
                .setLastModified(
                        LocalDateTime.parse(
                                "2024-03-13 13:00:12",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .setMessageBody("Hi");

        // then
        assertThat(byteArrayOutputStream.toString()).isEqualTo(
                """
                        HTTP/1.1 200 OK\r
                        Content-Type: text/html; charset=utf-8\r
                        Content-Length: 100\r
                        Location: /myLocation.html\r
                        Set-Cookie: myCookie=myValue; Path=/index.html;\r
                        Last-Modified: 2024-03-13T13:00:12\r
                        \r
                        Hi\r
                        """
        );
    }
}