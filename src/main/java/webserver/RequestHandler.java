package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            String fileName = getFileName(line);
            // InputStream 의 로그를 콘솔에 출력해 줍니다.
            logger.debug(line);
            while(!line.isEmpty()){
                line = br.readLine();
                logger.debug(line);
            }
            // html 파일을 모두 읽어와 Bytes 로 변환해준다.
            byte[] file = Files.readAllBytes(new File("./src/main/resources/static/" + fileName).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, file.length);
            responseBody(dos, file);
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
    public String getFileName(String firstLine) throws IOException {
        // /index.html HTTP/1.1 string 형태로 받아와서 파일 이름을 사용할수있게 split 을 이용하여 index.html 형태로 파싱해줍니다.
        String[] getFile = firstLine.split(" ");
        return getFile[1];
    }

}
