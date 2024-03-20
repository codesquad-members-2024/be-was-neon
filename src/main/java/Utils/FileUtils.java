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
     * @return 파일 내용을 담은 byte 배열. 파일 읽기 실패 시 빈 배열 반환.
     */
    public byte[] readFileToByteArray() {
        byte[] data = new byte[0]; // 결과 데이터를 담을 배열 초기화

        try (FileInputStream fis = new FileInputStream(this.file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024]; // 1KB 크기의 버퍼
            int readBytes;
            while ((readBytes = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, readBytes);
            }
            data = bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("파일을 찾을 수가 없습니다.: {}", e.getMessage());
            return new byte[0];
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            return new byte[0];
        }

        return data; // 메소드의 맨 마지막에서 정상 흐름의 결과 반환
    }
    /**
     * 파일의 내용을 byte 배열로 읽어서 반환하는 메소드를 RequestHandler에서 호출할 수 있도록 추가
     * @param filePath 파일 경로
     * @return 파일 내용을 담은 byte 배열. 파일 읽기 실패 시 예외 발생.
     * @throws FileNotFoundException 파일을 찾을 수 없을 경우 발생
     */
    public static byte[] readFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils fileUtils = new FileUtils(file);
        byte[] data = fileUtils.readFileToByteArray();
        if (data.length == 0) { // 파일 내용이 비어있거나 파일을 읽을 수 없는 경우
            logger.error("Requested file not found or empty: {}", filePath);
            throw new FileNotFoundException("File not found or empty: " + filePath);
        }
        return data;
    }
}
