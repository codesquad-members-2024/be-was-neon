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
     *
     * @return 파일 내용을 담은 byte 배열. 파일 읽기 실패 시 빈 배열 반환.
     */
    public byte[] readFileToByteArray() {
        try (FileInputStream fis = new FileInputStream(this.file)) {
            return readDataFromInputStream(fis);
        } catch (FileNotFoundException e) {
            logger.error("파일을 찾을 수 없습니다.: {}", this.file.getAbsolutePath(), e);
            return new byte[0];
        } catch (IOException e) {
            logger.error("IOException 발생: {}", e.getMessage(), e);
            return new byte[0];
        }
    }

    /**
     * 입력 스트림으로부터 데이터를 읽어 byte 배열로 반환하는 메소드.
     *
     * @param inputStream 읽을 입력 스트림
     * @return 입력 스트림의 내용을 담은 byte 배열. 읽기 실패 시 빈 배열 반환.
     */
    private byte[] readDataFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 1KB 크기의 버퍼
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, readBytes);
        }
        return bos.toByteArray();
    }

    public static byte[] readFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils fileUtils = new FileUtils(file);
        byte[] data = fileUtils.readFileToByteArray();
        if (data.length == 0) {
            logger.error("Requested file not found or empty: {}", filePath);
            throw new FileNotFoundException("File not found or empty: " + filePath);
        }
        return data;
    }
}