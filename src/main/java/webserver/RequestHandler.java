package webserver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import model.User;
import utils.Decoder;
import utils.DirectoryMatcher;
import utils.FileExtractor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private List<User> users;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        users = new ArrayList<>();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String request = br.readLine();
            logger.debug("request line : {}", request);

            String url = FileExtractor.extractUrl(request);
            String filePath = DirectoryMatcher.mathDirectory(url); // 디렉토리를 포함한 위치 지정
            if (url.matches("/\\w+/create\\?.+")) { // 생성 요청인 경우
                createUser(url);
                filePath = "src/main/resources/static/registration/register.html"; // User 생성 완료 후 다시 돌아가기
            }

            byte[] body = readAllBytes(filePath); // 바이트 배열로 변환
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void createUser(String url)
            throws IndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
        String[] userInfo = FileExtractor.extractUser(url);
        String id = Decoder.decode(userInfo[0]);
        String pw = Decoder.decode(userInfo[1]);
        String name = Decoder.decode(userInfo[2]);
        String email = Decoder.decode(userInfo[3]);
        User user = new User(id, pw, name, email);
        users.add(user);
        logger.debug(user.toString());
    }

    private byte[] readAllBytes(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()]; // 파일 길이만큼 바이트 배열 생성
            int bytesRead = inputStream.read(buffer); // 파일 내용 읽기

            if (bytesRead != file.length()) { // 잘못 변환된 경우 예외 처리
                throw new IOException();
            }
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
