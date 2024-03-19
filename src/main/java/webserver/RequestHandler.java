package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import Utils.FileUtils;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Utils.HttpRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Utils.PathParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String firstPath; // 첫 번째 요청된 경로(index.html)를 저장할 변수
    private String requestLine;
    private OutputStream out;


    public RequestHandler(Socket connectionSocket) {

        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = this.connection.getOutputStream()) {
            this.out = out; // OutputStream 저장
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 요청 처리
            handleRequest(br);

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
            // 파일을 읽어 바이트 배열로 변환 NIO 안 쓰기
            File file = new File("src/main/resources/static" + firstPath);
            byte[] body = FileUtils.readFileToByteArray(file);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleRequest(BufferedReader br) throws IOException {
        // 요청 라인 읽기
        this.requestLine = readRequestLine(br);
        // 요청 경로 파싱
        parseRequestPath();
        // 헤더 읽기
        readHeaders(br);

        // 회원가입 또는 파일 응답 처리
        handleResponse();
    }

    private void handleResponse() throws IOException {

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            HttpRequestParser httpRequestParser = new HttpRequestParser(requestLine);
            Optional<User> userOptional = httpRequestParser.parseUserFromGetRequest();

            if (userOptional.isPresent()) {
                handleUserRequest(userOptional.get());
            } else {
                handleFileRequest(httpRequestParser);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void handleUserRequest(User user) {
        // 회원가입 처리 로직
        logger.debug("회원가입 요청 처리: {}", user);
    }

    private void handleFileRequest(HttpRequestParser httpRequestParser) throws IOException {
        String path = httpRequestParser.makePath();
        firstPath = path;
        byte[] body = readFileContent();
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, firstPath);
        responseBody(dos, body);
    }

    private String readRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        logger.debug("request line: {}", line);
        return line;
    }

    private void parseRequestPath() {
        HttpRequestParser httpRequestParser = new HttpRequestParser(requestLine);
        String path = httpRequestParser.makePath();

        firstPath = path;
        logger.debug("Extracted path: {}", firstPath);
    }

    private void readHeaders(BufferedReader br) throws IOException {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            logger.debug("header: {}", line);
        }
    }

    private byte[] readFileContent() throws IOException {
        FileUtils fileUtils = new FileUtils(new File(firstPath));
        byte[] data = fileUtils.readFileToByteArray();
        if (data.length == 0) { // 파일 내용이 비어있거나 파일을 읽을 수 없는 경우
            logger.error("Requested file not found or empty: {}", firstPath);
            throw new FileNotFoundException("File not found or empty: " + firstPath);
        }
        return data;
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
<<<<<<< HEAD

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
                // 일단 html과 식별 되지 안는 확장자 파일 동시처리

        }
    }

}
=======
}
>>>>>>> 30cc36eff70d8e8dfd1147d55f525c7a02e544aa
