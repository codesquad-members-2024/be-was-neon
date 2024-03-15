package utils;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    public static String getFileName(String firstLine){
        // /index.html HTTP/1.1 string 형태로 받아와서 파일 이름을 사용할수있게 split 을 이용하여 index.html 형태로 파싱해줍니다.
        String[] getFile = firstLine.split(" ");
        return getFile[1];
    }
    public static void showEveryHeaders(Logger logger, BufferedReader br, String line) throws IOException {
        while (line != null && !line.isEmpty()) {
            // Request header 의 로그를 콘솔에 출력해 줍니다.
            logger.debug("header : {}",line);
            line = br.readLine();
        }
    }
}
