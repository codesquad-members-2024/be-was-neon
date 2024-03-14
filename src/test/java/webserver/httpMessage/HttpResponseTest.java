package webserver.httpMessage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class HttpResponseTest {

    public static final String INDEX_HTML_PATH = "src/main/resources/static/index.html";

    @Test
    @DisplayName("HttpResponse는 file 내용을 body로 가지고 있어야 한다.")
    void from() throws IOException {
        File file = new File(INDEX_HTML_PATH);
        HttpResponse actual = HttpResponse.from(file);

        byte[] expected = Files.readAllBytes(Path.of(INDEX_HTML_PATH));
        Assertions.assertThat(actual.getBody()).isEqualTo(expected);
    }

}