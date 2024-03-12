package webserver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
    private static final String CREATE_REQUEST = "/\\w+/create\\?.+";
    private static final String NEW_CLIENT_CONNECT_MESSAGE = "New Client Connect! Connected IP : {}, Port : {}";
    private static final String REQUEST_LINE_MESSAGE = "request line : {}";
    private static final String CHARSETS = "UTF-8";
    private static final int ID_INDEX = 0;
    private static final int PW_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int EMAIL_INDEX = 3;

    private List<User> users;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        users = new ArrayList<>();
    }

    public void run() {
        logger.debug(NEW_CLIENT_CONNECT_MESSAGE, connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, CHARSETS);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String request = br.readLine();
            logger.debug(REQUEST_LINE_MESSAGE, request);

            String url = FileExtractor.extractUrl(request);
            byte[] body = generateByte(url);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] generateByte(String url) throws IOException {
        String filePath = DirectoryMatcher.mathDirectory(url); // 디렉토리를 포함한 위치 지정
        if (url.matches(CREATE_REQUEST)) { // 생성 요청인 경우
            createUser(url);
            return new byte[0]; // 페이지 이동 X (임시)
        } else {
            return readAllBytes(filePath); // 바이트 배열로 변환
        }
    }

    private void createUser(String url)
            throws IndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
        String[] userInfo = FileExtractor.extractUser(url);
        String id = Decoder.decode(userInfo[ID_INDEX]);
        String pw = Decoder.decode(userInfo[PW_INDEX]);
        String name = Decoder.decode(userInfo[NAME_INDEX]);
        String email = Decoder.decode(userInfo[EMAIL_INDEX]);
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
