package webserver;

import http.ContentType;
import http.FileReader;
import http.HttpMethod;
import http.HttpRequestVerifier;
import http.HttpStatus;
import http.ResponseBody;
import http.ResponseHeader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.User;
import utils.Decoder;
import utils.DirectoryMatcher;
import utils.Extractor;
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
    private static final String ERROR_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String CONTENT_TYPE_CHARSET = ";charset=utf-8";
    private static final String H1_FORMAT = "<h1>%s</h1>";
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

            byte[] body = processRequest(request, responseHeader);
            sendResponse(body, responseBody, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] processRequest(String request, ResponseHeader responseHeader) throws IOException {
        String method = Extractor.extractMethod(request);
        if (method.equals(HttpMethod.GET.name())) { // 우선 GET 요청만 처리
            return processGETRequest(request, responseHeader);
        }
        return new byte[0];
    }

    private byte[] processGETRequest(String request, ResponseHeader responseHeader) throws IOException {
        String[] requestHeader = Extractor.extract(request);
        String url = Extractor.extractUrl(request);
        String filePath = DirectoryMatcher.mathDirectory(url);
        if (url.matches(CREATE_REQUEST)) { // user 생성(회원 가입) 요청인 경우
            String[] userEncodedInfo = Extractor.extractUser(url);
            createUser(userEncodedInfo);
            filePath = String.format(H1_FORMAT, HttpStatus.OK.getDescription());
            setResponseHeader(responseHeader, HttpStatus.OK, ERROR_CONTENT_TYPE, 0);
        } else if (!HttpRequestVerifier.verify(requestHeader)) { // 올바른 http request가 아닌 경우
            filePath = String.format(H1_FORMAT, HttpStatus.BAD_REQUEST.getDescription());
            setResponseHeader(responseHeader, HttpStatus.BAD_REQUEST, ERROR_CONTENT_TYPE, filePath.getBytes().length);
        } else if (!HttpRequestVerifier.verifyFile(filePath)) { // 파일을 못 찾거나 파일이 아닌 경우
            filePath = String.format(H1_FORMAT, HttpStatus.NOT_FOUND.getDescription());
            setResponseHeader(responseHeader, HttpStatus.NOT_FOUND, ERROR_CONTENT_TYPE, filePath.getBytes().length);
        } else { // 올바른 GET 요청인 경우
            String contentType = getContentType(filePath);
            setResponseHeader(responseHeader, HttpStatus.OK, contentType, FileReader.readAllBytes(filePath).length);
        }
        return FileReader.readAllBytes(filePath); // 바이트 배열로 변환
    }

    private void setResponseHeader(ResponseHeader responseHeader, HttpStatus httpStatus, String contentType, int length) {
        responseHeader.setStatus(httpStatus);
        responseHeader.setContentType(contentType);
        responseHeader.setLengthOfBodyContent(length);
    }

    private String getContentType(String filePath) {
        List<String> contentTypes = List.of(ContentType.html.getContentType(), ContentType.css.getContentType(), ContentType.js.getContentType());
        Path path = Paths.get(filePath);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
            if (contentTypes.contains(contentType)) {
                return contentType + CONTENT_TYPE_CHARSET; // utf-8 charset 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }

    private void createUser(String[] userEncodedInfo)
            throws IndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
        String[] userInfo = Decoder.decodeUser(userEncodedInfo);
        String id = userInfo[ID_INDEX];
        String pw = userInfo[PW_INDEX];
        String name = userInfo[NAME_INDEX];
        String email = userInfo[EMAIL_INDEX];
        User user = new User(id, pw, name, email);
        users.add(user);
        logger.debug(user.toString());
    }

    private void sendResponse(byte[] body, ResponseBody responseBody, DataOutputStream dos) throws IOException {
        responseBody.createBody(body);
        dos.flush();
    }
}
