package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URL;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            HttpRequest httpRequest = new HttpRequest(br);
            String uri = httpRequest.getTargetURI();

            File file = URL.getFile(uri);

            String content = "text/html";
            if (uri.endsWith("css")) {
                content = "text/css";
            } else if (uri.endsWith("svg")) {
                content = "image/svg+xml";
            } else if (uri.endsWith("ico")) {
                content = "image/vnd.microsoft.icon";
            } else if (uri.endsWith("js")) {
                content = "text/javascript";
            }


            FileInputStream fis = new FileInputStream(file);
            byte[] body = fis.readAllBytes();

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, content);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        String type = "";
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String content) {
        String type = "";
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+content+";charset=utf-8\r\n");
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
