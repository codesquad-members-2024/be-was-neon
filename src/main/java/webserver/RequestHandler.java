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
    private static final String HTML_EXTENSION = ".html";
    private static final String ROOT = "/";
    private static final String CSS_EXTENSION = ".css";
    private static final String ICO_EXTENSION = ".ico";
    private static final String SVG_EXTENSION = ".svg";
    private static final String BASE_NAME = "index";
    private static final int BUFFER_SIZE = 1024;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String request = requestParse(line);

                urlMapper(request, dos);

                line = reader.readLine();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void urlMapper(String request, DataOutputStream dos) {
        if (request.equals(ROOT)) {
            responseHtml(dos, RESOURCE_PATH + request + BASE_NAME + HTML_EXTENSION);
            return;
        }
        if (request.endsWith(HTML_EXTENSION)) {
            responseHtml(dos, RESOURCE_PATH + request);
            return;
        }
        if (request.endsWith(CSS_EXTENSION)) {
            return;
        }
        if (request.endsWith(SVG_EXTENSION)) {
            return;
        }
        if (request.endsWith(ICO_EXTENSION)) {
            return;
        }
        if (!request.isEmpty()) {
            responseHtml(dos, RESOURCE_PATH + request + ROOT + BASE_NAME + HTML_EXTENSION);
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
            byte[] buffer = new byte[BUFFER_SIZE];

            // HTML 파일을 읽어 ByteArrayOutputStream에 쓰기
            while ((bytesRead = fileInputStream.read(buffer)) != -1) { // 더 이상 읽지 못하면 -1을 반환 -> 루프 종료
                byteArrayOutputStream.write(buffer, 0, bytesRead); // buffer 배열의 0번째부터 읽은 바이트 수 만큼 작성
            }

            // ByteArrayOutputStream의 내용을 바이트 배열로 가져오기
            byte[] fileBytes = byteArrayOutputStream.toByteArray();  // 모든 html 내용이 쓰여져 있음 (메모리에 올라온 상태)

            // DataOutputStream으로 변환하여 반환
            response200Header(dos, fileBytes.length);
            responseBody(dos, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
