package webserver;

import db.Database;
import java.io.*;
import java.net.Socket;

import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ParameterParser;
import utils.PathHandler;
import utils.RequestLineParser;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String USER_CREATE_PATH = "/user/create";
    private static final String STATIC_RESOURCES_PATH = "src/main/resources/static";
    private static final String COMMON_FILE = "/index.html";
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 클라이언트 요청 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                logger.debug("request header : {}", line);
                requestBuilder.append(line).append("\r\n");
            }

            String requestPath = RequestLineParser.extractPath(requestBuilder.toString());

            // 회원가입 관련 요청일 경우, 사용자 입력 정보 파싱 후 User 객체 생성 및 저장
            if (requestPath.contains(USER_CREATE_PATH)) {
                Database.addUser(parseUser(requestPath));
            }

            sendResponse(requestPath, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private User parseUser(String requestPath) {
        Map<String, String> userInfo = ParameterParser.getUserParams(validateCreateUserPath(requestPath));
        return new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
    }

    private String validateCreateUserPath(String createUserPath) {
        String[] pathAndInfo = createUserPath.split("\\?");
        if (pathAndInfo.length != 2 || !pathAndInfo[0].equals("/user/create")) {
            throw new IllegalArgumentException("INVALID PATH");
        }
        return pathAndInfo[1];
    }

    private void sendResponse(String requestPath, OutputStream out) throws IOException {
        // 파일 읽기
        byte[] body;
        try (FileInputStream fileInputStream = new FileInputStream(PathHandler.getStaticPath(requestPath))) {
            body = fileInputStream.readAllBytes();
        }

        // 응답 보내기
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;\r\n");
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