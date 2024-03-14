package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    public static byte[] readAllBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) { // 파일이 아닌 경우 오류메시지 바이트 배열 생성
            return filePath.getBytes();
        }
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
