package http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileReaderTest {
    @DisplayName("파일이나 오류메시지를 읽어 바이트 배열을 생성할 수 있다.")
    @Test
    void readAllBytes() throws IOException {
        String errorHtml = "<h1>Not Found</h1>";
        byte[] nonExistingFileBytes = FileReader.readAllBytes(errorHtml);
        assertThat(nonExistingFileBytes).isNotNull();
        assertThat(new String(nonExistingFileBytes)).isEqualTo(errorHtml);

        String filePath = "<h1>Not Found</h1>";
        byte[] existingFileBytes = FileReader.readAllBytes(filePath);
        assertThat(existingFileBytes).isNotNull();
        assertThat(new String(existingFileBytes)).isEqualTo(filePath);
    }
}