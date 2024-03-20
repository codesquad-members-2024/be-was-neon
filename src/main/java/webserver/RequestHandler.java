package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.RequestUtils.extractRequestURL;
import static webserver.RequestUtils.requestLineParser;
import static webserver.ResponseHandler.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final String INDEX_FILE_PATH = "src/main/resources/static";
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

    private void handleException(IOException e) {
        logger.error("Error handling client request", e);
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
        //컨텐츠 타입 지원 구현
        ContentTypeHandler contentTypeHandler = new ContentTypeHandler();
        contentTypeHandler.handleContent(requestLine, out);


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
}
