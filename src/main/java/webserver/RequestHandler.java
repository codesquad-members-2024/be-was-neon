package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    String INDEX_FILE_PATH = "src/main/resources/static/index.html";
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
            String line = br.readLine();
            logger.debug("request line : {}", line);
            while (!line.equals("")) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            File file = new File(INDEX_FILE_PATH);
            if (file.exists() && file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] body = new byte[(int) file.length()];
                fis.read(body);

                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);

                fis.close();
            } else {
                logger.error("File not found: {}", INDEX_FILE_PATH);
                response404(out);
            }
        } catch (IOException e) {
            logger.error("Error handling client request", e);
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

    private void response404(OutputStream out) {
        try {
            String errorMessage = "<h1>404 Not Found</h1>";
            byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
            out.write("HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8));
            out.write("Content-Type: text/html; charset=utf-8\r\n".getBytes(StandardCharsets.UTF_8));
            out.write(("Content-Length: " + errorBytes.length + "\r\n").getBytes(StandardCharsets.UTF_8));
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            out.write(errorBytes);
            out.flush();
        } catch (IOException e) {
            logger.error("Error sending 404 response", e);
        }
    }
}
