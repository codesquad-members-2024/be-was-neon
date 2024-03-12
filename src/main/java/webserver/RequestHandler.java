package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.RequestUtils.extractRequestURL;

public class RequestHandler implements Runnable {
    private static final String INDEX_FILE_PATH = "src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            logger.debug("request line : {}", line);
            while (!line.equals("") && line.isEmpty()) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            String requestLine = extractRequestURL(line);
            File file = new File(INDEX_FILE_PATH + requestLine);
            if (file.isDirectory()) {
                file = new File(file, "/index.html");
            }
            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] body = new byte[(int) file.length()];
                fis.read(body);

                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
                fis.close();
            } else{
                logger.error("File not found: {}", INDEX_FILE_PATH + requestLine);
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
