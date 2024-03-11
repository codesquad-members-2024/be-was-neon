package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final String BLANK = "\s+";
    private static final String BASE_PATH = "./src/main/resources/static";
    private static final int URL_INDEX = 1;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String filePath = br.readLine();

            logger.debug("request line : {}", filePath);
            String url = filePath.split(BLANK)[URL_INDEX];
            byte[] body = readAllBytes(BASE_PATH + url);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] readAllBytes(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = inputStream.read(buffer); // 파일 내용 읽기

            if (bytesRead != file.length()) {
                throw new IOException();
            }
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
