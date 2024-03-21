package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ResponseHandler {
    static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);
    public static void sendResponse(OutputStream out, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        }
    }
    public static void sendResponseContentType(OutputStream out, File file, ContentType contentType) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            DataOutputStream dos = new DataOutputStream(out);
            response200HeaderContent(dos, body.length,contentType );
            responseBody(dos, body);
        }
    }

    public static void response302Header(DataOutputStream dos, String location) {
        // 302 response 는 get 으로 유저 데이터를 받아온후에 redirect 하여 클라이언트를 로그인 페이지로 연결시켜줍니다.
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response200HeaderContent(DataOutputStream dos, int lengthOfBodyContent, ContentType contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType.getValue() + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response404(OutputStream out) {
        try {
            String errorMessage = "<h1>404 Not Found</h1>";
            byte[] errorBytes = errorMessage.getBytes("UTF-8");

            // 응답 헤더
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("HTTP/1.1 404 Not Found\r\n");
            responseBuilder.append("Content-Type: text/html; charset=utf-8\r\n");
            responseBuilder.append("Content-Length: ").append(errorBytes.length).append("\r\n");
            responseBuilder.append("\r\n");

            // 응답 헤더 및 본문을 한 번에 보내기
            out.write(responseBuilder.toString().getBytes("UTF-8"));
            out.write(errorBytes);
            out.flush();
        } catch (IOException e) {
            logger.error("Error sending 404 response", e);
        }
    }
}
