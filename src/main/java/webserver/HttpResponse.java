package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static void sendHttpResponse(OutputStream out, String filePath) {
        DataOutputStream dos = new DataOutputStream(out);

        byte[] body = readFile(filePath);
        String contentType = getContentType(filePath);
        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    public static void redirectHttpResponse(OutputStream out, String filePath) {
        DataOutputStream dos = new DataOutputStream(out);

        response300Redirect(dos, filePath);
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent,
        String contentType) {
        try {
            // 캐릭터 라인을 기본이 되는 출력 스트림에 일련의 바이트로서 출력합니다.
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error("response200HeaderError : " + e.getMessage());
        }
    }

    private static void response300Redirect(DataOutputStream dos, String location) {
        try {
            // 캐릭터 라인을 기본이 되는 출력 스트림에 일련의 바이트로서 출력합니다.
            dos.writeBytes("HTTP/1.1 301 Moved Permanently\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error("response300HeaderError : " + e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            //지정된 바이트 배열의 오프셋(offset) 위치 off로 부터 시작되는 len 바이트를 기본이 되는 출력 스트림에 출력합니다.
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error("responseBodyError : " + e.getMessage());
        }
    }

    private static byte[] readFile(String path) {

        File file = new File(path);
        byte[] byteArray = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArray;
    }

    private static String getContentType(String filePath) {
        String contentType = "application/octet-stream";

        if (filePath.endsWith(".html")) {
            contentType = "text/html;charset=utf-8";
        }
        if (filePath.endsWith(".css")) {
            contentType = "text/css";
        }
        if (filePath.endsWith(".js")) {
            contentType = "text/javascript";
        }
        if (filePath.endsWith(".png")) {
            contentType = "image/png";
        }
        if (filePath.endsWith(".jpg")) {
            contentType = "image/jpeg";
        }
        if (filePath.endsWith(".ico")) {
            contentType = "image/x-icon";
        }
        if (filePath.endsWith(".svg")) {
            contentType = "image/svg+xml";
        }

        return contentType;
    }
}
