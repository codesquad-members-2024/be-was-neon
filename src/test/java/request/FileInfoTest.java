package request;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class FileInfoTest {

    @Test
    @DisplayName("static 폴더에 있는 파일의 경로를 반환해야 한다.")
    void getCompletePath() {
        String completePath = FileInfo.getCompletePath("/registration");
        assertThat(completePath).isEqualTo("src/main/resources/static/registration/index.html");
    }

    @Test
    @DisplayName("파일 확장자를 반환해야 한다.")
    void getFileType(){
        String fileType = FileInfo.getFileType("src/main/resources/static/registration/index.html");
        assertThat(fileType).isEqualTo("html");
    }
}
