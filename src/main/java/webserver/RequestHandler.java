package webserver;

import static utils.RequestHeaderParser.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String RESOURCE_PATH = "./src/main/resources/static";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            String line = reader.readLine();
            String request = requestParse(line);
            while (!line.isEmpty()) {
                if (request.endsWith(".html")) {
                    responseHtml(dos, RESOURCE_PATH + request);
                }
                line = reader.readLine();
                request = requestParse(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseHtml(DataOutputStream dos, String path) {
        try (
                FileInputStream fileInputStream = new FileInputStream(path);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ) {
            int bytesRead;
            byte[] buffer = new byte[1024];

            // HTML 파일을 읽어 ByteArrayOutputStream에 쓰기
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // ByteArrayOutputStream의 내용을 바이트 배열로 가져오기
            byte[] fileBytes = byteArrayOutputStream.toByteArray();

            // DataOutputStream으로 변환하여 반환
            response200Header(dos, fileBytes.length);
            responseBody(dos, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
