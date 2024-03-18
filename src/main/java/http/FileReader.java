package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    private File file;

    public FileReader(File file) {
        this.file = file;
    }

    public byte[] readAllBytes() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()]; // 파일 길이만큼 바이트 배열 생성
            int bytesRead = inputStream.read(buffer); // 파일 내용 읽기

            if (bytesRead != file.length()) { // 잘못 변환된 경우 예외 처리
                throw new IOException();
            }
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
