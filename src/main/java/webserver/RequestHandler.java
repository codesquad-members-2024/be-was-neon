package webserver;

import http.FileReader;
import http.HttpRequestVerifier;
import http.ResponseBody;
import http.ResponseHeader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import utils.Decoder;
import utils.DirectoryMatcher;
import utils.FileExtractor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHeader responseHeader = new ResponseHeader(dos);
            ResponseBody responseBody = new ResponseBody(dos);

            verifyRequest(dos, request, responseHeader, responseBody);
            sendResponse(request, responseHeader, responseBody, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void verifyRequest(DataOutputStream dos, String request, ResponseHeader responseHeader, ResponseBody responseBody)
            throws IOException {
        String[] requestHeader = FileExtractor.extract(request);
        if (!HttpRequestVerifier.verify(requestHeader)) {
            responseHeader.createHeader(400, "Bad Request", "text/plain;charset=utf-8", 0);
            responseBody.createBody(new byte[0]);
            dos.flush();
        }
    }

    private byte[] processRequest(String request) throws IOException {
        String url = FileExtractor.extractUrl(request);
        if (url.matches(CREATE_REQUEST)) { // 생성 요청인 경우
            String[] userInfo = FileExtractor.extractUser(url);
            createUser(userInfo);
            return new byte[0]; // 페이지 이동 X (임시)
        }
        String filePath = DirectoryMatcher.mathDirectory(url);
        return FileReader.readAllBytes(filePath); // 바이트 배열로 변환
    }

    private void createUser(String[] userInfo)
            throws IndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
        String id = Decoder.decode(userInfo[ID_INDEX]);
        String pw = Decoder.decode(userInfo[PW_INDEX]);
        String name = Decoder.decode(userInfo[NAME_INDEX]);
        String email = Decoder.decode(userInfo[EMAIL_INDEX]);
        User user = new User(id, pw, name, email);
        users.add(user);
        logger.debug(user.toString());
    }

    private void sendResponse(String request, ResponseHeader responseHeader, ResponseBody responseBody, DataOutputStream dos)
            throws IOException {
        byte[] body = processRequest(request);
        responseHeader.createHeader(200, "OK", "text/html;charset=utf-8", body.length);
        responseBody.createBody(body);
        dos.flush();
    }
}
