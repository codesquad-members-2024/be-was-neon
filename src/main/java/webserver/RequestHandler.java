package webserver;

import Utils.FileUtils;
import Utils.HttpRequestParser;
import Utils.RouteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String firstPath;
    private OutputStream out;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = this.connection.getOutputStream()) {
            this.out = out; // OutputStream 저장
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 요청 처리
            handleRequest(br);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private void handleRequest(BufferedReader br) throws IOException {
        String httpRequest = readFullHttpRequest(br);
        HttpRequestParser httpRequestParser = new HttpRequestParser(httpRequest); // 파싱 로직은 HttpRequestParser로 이동

        String path = httpRequestParser.extractPath();
        firstPath = RouteManager.makePath(path);
        logger.debug("Extracted path: {}", firstPath);

        // FileUtils 사용하여 파일 내용 읽기
        byte[] body = FileUtils.readFileContent(firstPath); // 수정된 부분
        handleResponse(body);
    }


    private String readFullHttpRequest(BufferedReader br) throws IOException {
        // 전체 HTTP 요청을 문자열로 읽어서 반환하는 로직 구현
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }
        // 요청 바디가 있다면 여기에서 추가 처리
        return requestBuilder.toString();
    }


    private void handleResponse(byte[] body) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            response200Header(dos, body.length, firstPath);
            responseBody(dos, body);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String filePath) throws IOException {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        String contentType = determineContentType(extension);

        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }


    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private String determineContentType(String extension) {
        switch (extension) {
            case ".css":
                return "text/css;charset=utf-8";
            case ".js":
                return "application/javascript;charset=utf-8";
            case ".png":
                return "image/png";
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".ico":
                return "image/x-icon";
            case ".svg":
                return "image/svg+xml";
            case ".html":
            default:
                return "text/html;charset=utf-8";
            // html과 식별 되지 안는 확장자 파일 임시 동시처리

        }
    }
}