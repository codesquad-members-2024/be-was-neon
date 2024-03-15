package webserver;

import java.io.*;
import java.net.Socket;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.RequestUtils.extractRequestURL;
import static webserver.RequestUtils.requestLineParser;

public class RequestHandler implements Runnable {
    private static final String INDEX_FILE_PATH = "src/main/resources/static";
    static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            handleRequest(in, out);
        } catch (IOException e) {
            handleException(e);
        }
    }
    private void handleRequest(InputStream in, OutputStream out) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        logger.debug("request line : {}", line);

        while (!line.equals("") && line.isEmpty()) {
            line = br.readLine();
            logger.debug("header : {}", line);
        }

        String requestLine = extractRequestURL(line);
        if (line.startsWith("GET /create?")) {
            registrationHandler(out, line);
            return;
        }
        File file = new File(INDEX_FILE_PATH + requestLine);
        if (file.isDirectory()) {
            file = new File(file, "/index.html");
        }
        if (file.exists()) {
            sendResponse(out, file);
        } else {
            logger.error("File not found: {}", INDEX_FILE_PATH + requestLine);
            response404(out);
        }


    }


    private void registrationHandler(OutputStream out, String line) {
        if (line.startsWith("GET")) {
            // GET 요청에 대한 처리
            requestLineParser(line);
            // 회원가입 성공 시 로그인 페이지로 리다이렉트
            DataOutputStream dos = new DataOutputStream(out);
            response302Header(dos, "/index.html");
        } else {
            // GET 요청이 아닌 경우 404 응답
            response404(out);
        }
    }


    private void sendResponse(OutputStream out, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        }
    }

    private void handleException(IOException e) {
        logger.error("Error handling client request", e);
    }

    private static void response302Header(DataOutputStream dos, String location){
        // 302 response 는 get 으로 유저 데이터를 받아온후에 redirect 하여 클라이언트를 로그인 페이지로 연결시켜줍니다.
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");

        }catch (IOException e) {
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
