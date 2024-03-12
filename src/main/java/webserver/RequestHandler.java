package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Pattern getFileHeader = Pattern.compile("(GET )((\\/?.)*)(\\.+\\w)?( HTTP\\/1.1)");
    private final Pattern registrationHeader = Pattern.compile("(GET )(\\/registration)( HTTP\\/1.1)");
    private final Pattern createUserHeader = Pattern.compile("(GET \\/create\\?userId\\=)+(\\w)+(&password=)+(\\w)+(&name=)(\\w)+(&email=)(.*%40\\w+\\.com)( HTTP\\/1.1)");

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        String requestHeader;
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            requestHeader = br.readLine();
            String[] request = requestHeader.split(" ");

            if (createUserHeader.matcher(requestHeader).matches()) {
                log.info("create request " + requestHeader);
                createUser(dos, requestHeader);
            } else if (getFileHeader.matcher(requestHeader).matches()) {
                log.info("getFile request : " + requestHeader);
                responseGetFile(dos, request);
            } else {
                log.error(requestHeader);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) { // 리퀘스트 읽을 때 발생 예외
            log.error(e.getMessage());
        }
    }

    private static void createUser(DataOutputStream dos, String requestHeader) throws IOException {
        // create User 메서드
        Pattern param = Pattern.compile("=\\w+(%40\\w+\\.com)?");
        Matcher matcher = param.matcher(requestHeader);
        List<String> createParameters = new ArrayList<>();

        while (matcher.find()) {
            createParameters.add(matcher.group());
        }

        String id = createParameters.get(0).substring(1);
        String password = createParameters.get(1).substring(1);
        String nickname = createParameters.get(2).substring(1);
        String email = createParameters.get(3).substring(1);

        User user = new User(id, password, nickname, email);
        Database.addUser(user);

        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location:" + "/index.html");
        dos.writeBytes("\r\n");

        log.info("created : " + Database.findUserById(id).toString());
    }

    private static void responseGetFile(DataOutputStream dos, String[] request) throws IOException {
        // 리퀘스트를 파싱해 타입, 요청한 파일 경로 , 파일 확장자 얻음
        String requestType = request[0];
        String url = request[1];
        String fileType = "html";

        try { // 파일 이름이 아닌 경로로만 요청이 들어왔을 때 그 경로의 index.html 선택
            fileType = url.split("\\.")[1];
        } catch (ArrayIndexOutOfBoundsException justPath) {
            url = url + "/index.html";
        }
        log.info(requestType + " : " + url);

        try {
            byte[] body = responseBodyFile(url);
            response200Header(dos, body.length, FileType.valueOf(fileType.toUpperCase()).getContentType());
            responseBody(dos, body);
        } catch (IOException noSuchFile) { // 해당 경로의 파일이 없을때
            response404(dos);
            log.error(noSuchFile.getMessage());
        }
    }

    private static byte[] responseBodyFile(String url) throws IOException {
        File file = new File("src/main/resources/static" + url);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        return bytes;
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response404(DataOutputStream dos) throws IOException {
        String errorMessage = "404 Not Found";
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/plain\r\n");
        dos.writeBytes("Content-Length: " + errorMessage.length() + "\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes(errorMessage); // body
    }
}