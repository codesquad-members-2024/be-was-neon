package webserver;

import static utils.RequestHeaderParser.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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

            //TODO: 통째로 받아서 convert 하기
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
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) { // FileInputStream을 사용하여 파일 열기
            byte[] byteArray = new byte[(int) file.length()]; // 파일의 크기만큼의 바이트 배열 생성

            fis.read(byteArray); // 파일 내용을 바이트 배열에 읽어들임

            response200Header(dos, byteArray.length);
            responseBody(dos, byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
