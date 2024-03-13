package webserver;

import java.io.*;
import java.net.Socket;

import Utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Utils.PathParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String firstPath; // 첫 번째 요청된 경로(index.html)를 저장할 변수

    public RequestHandler(Socket connectionSocket) {

        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            logger.debug("request line: {}", line);

            // 요청된 URL
            String path = PathParser.extractPathFromRequestLine(line);
            if ("/index.html".equals(path)) {
                if (firstPath == null) {
                    firstPath = path;
                    logger.debug("Extracted path: {}", firstPath);
                }
            }

            while(!line.isEmpty()){
                line = br.readLine();
                logger.debug("header: {}", line);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // 파일을 읽어 바이트 배열로 변환 (NIO 안 쓰기)
            File file = new File("src/main/resources/static" + firstPath);

            // FileUtils 인스턴스 생성
            FileUtils fileUtils = new FileUtils(file);
            byte[] body = fileUtils.readFileToByteArray(); // 인스턴스 메소드 호출

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);

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
}