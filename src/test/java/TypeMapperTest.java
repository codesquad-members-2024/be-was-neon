import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.utils.TypeMapper;

import java.io.File;

public class TypeMapperTest {

    private static final String STATIC_PATH = "src/resource/static";


    @DisplayName("파일에 따라 정해진 Mime 타입을 반환해야 한다.")
    @ParameterizedTest
    @CsvSource(value = {"index.html,text/html", "main.css, text/css", "favicon.ico, image/x-icon", "img/like.svg, image/svg+xml"})
    void typeTest(String filePath, String expectedType) {
        File file = new File(STATIC_PATH + filePath);
        String contentType = TypeMapper.getContentType(file);

        Assertions.assertThat(contentType).isEqualTo(expectedType);
    }
}
