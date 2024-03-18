package http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileReaderTest {
    @DisplayName("파일을 읽어 바이트 배열을 생성할 수 있다.")
    @Test
    void readAllBytes() throws IOException {
        String filePath = "src/main/resources/static/index.html";
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        byte[] fileBytes = fileReader.readAllBytes();
        byte[] expectedBytes = Files.readAllBytes(Paths.get(filePath));
        assertThat(fileBytes).isNotNull();
        assertThat(fileBytes).isEqualTo(expectedBytes);
    }
}