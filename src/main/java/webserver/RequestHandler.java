package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class RequestHandler implements Runnable {

    private static final String STATIC_PATH = "src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            logger.debug(line);
            String[] startLine = line.split(" ");
            while (!(line = br.readLine()).isEmpty()) {
                logger.debug("line = " + line);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            String filePath;
            if (startLine[1].contains(".")) {
                filePath = STATIC_PATH + startLine[1];
            } else {
                filePath = STATIC_PATH + startLine[1] + "/index.html";
            }

            File file = new File(filePath);
            byte[] body = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(body);
            }

            String[] splitFilePath = filePath.split("/");
            String fileName = splitFilePath[splitFilePath.length - 1];
            String contentType = getContentType(fileName.split("\\.")[1]);

            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String type) {
        String contentType;
        if (type.equals("html") || type.equals("css")) {
            contentType = "text/" + type;
        } else if (type.equals("svg")) {
            contentType = "image/svg+xml";
        } else if (type.equals("ico")) {
            contentType = "image/x-icon";
        } else {
            throw new IllegalArgumentException("");
        }

        return contentType;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
