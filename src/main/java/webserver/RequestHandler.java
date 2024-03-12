package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.StringUtils;

public class RequestHandler implements Runnable {
    private static final String FILE_PATH = "src/main/resources/static";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String requestLine = br.readLine();
            logger.debug("request method : {}", requestLine);

            // 요청 받은 URL을 파싱하여 파일 경로를 결정한다.
            String requestURL = StringUtils.separatePath(requestLine);
            String filePath = FILE_PATH + requestURL;

            // 파일이 존재하면 해당 파일을 읽어 응답.
            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);
            } else {
                // 파일이 존재하지 않으면 404 응답.
                byte[] notFoundContent = "<h1>404 Not Found</h1>".getBytes();
                response404Header(dos, notFoundContent.length);
                responseBody(dos, notFoundContent);
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

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
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
