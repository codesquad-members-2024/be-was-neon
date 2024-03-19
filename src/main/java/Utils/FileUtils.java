package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private File file;

    public FileUtils(File file) {
        this.file = file;
    }

    /**
     * 설정된 파일의 내용을 byte 배열로 반환하는 메소드.
     * 대용량 파일 처리를 위해 파일을 청크 단위로 읽어 처리한다.
     *
     * @return 파일 내용을 담은 byte 배열.
     */
    public byte[] readFileToByteArray() {
        try (FileInputStream fis = new FileInputStream(this.file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024]; // 1KB 크기의 버퍼
            int readBytes;
            while ((readBytes = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, readBytes);
            }
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("파일을 찾을 수가 없습니다.: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
        }
        // 예외 발생 시 빈 배열 반환
        return new byte[0];
    }
}
=======

import java.io.*;

public class FileUtils{
    /**
     * 파일을 받아서 해당 파일의 내용을 byte 배열로 반환하는 메소드.
     *
     * @param file 읽고자 하는 파일 객체.
     * @return 파일 내용을 담은 byte 배열.
     */
    private static Logger logger;

    public static byte[] readFileToByteArray(File file) {

    FileInputStream content = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    try {
        content = new FileInputStream(file);
        int read;
        while ((read = content.read(buf)) != -1) {
            bos.write(buf, 0, read);
        }
    } catch (FileNotFoundException e) {
        logger.error("파일을 찾을 수가 없습니다.: {}", e.getMessage());
    } catch (IOException e) {
        logger.error("IOException: {}", e.getMessage());
    } finally {
        try {
            if (content != null) content.close();
        } catch (IOException e) {
            logger.error("파일 입력 스트림을 닫는 과정에서 오류가 발생했습니다.: {}", e.getMessage());
        }
    }
    return bos.toByteArray();
}
}
