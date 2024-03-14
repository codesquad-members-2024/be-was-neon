package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResourceHandler.class);
    public static String RESOURCE_PATH = "./src/main/resources/static";
    public static String BASE_NAME = "index";
    public static String HTML_EXTENSION = ".html";

    public static byte[] read(String filePath) {
        File file = new File(filePath);
        byte[] byteArray = new byte[(int) file.length()]; // 파일의 크기만큼의 바이트 배열 생성

        // FileInputStream을 사용하여 파일 열기
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(byteArray); // 파일 내용을 바이트 배열에 읽어들임
        } catch (IOException e) {
            logger.error("[RESOURCE HANDLER ERROR] {}", e.getMessage());
        }

        return byteArray;
    }
}
