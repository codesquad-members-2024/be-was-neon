package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private Pattern getFileHeader = Pattern.compile("(GET )((\\/?.)*)\\.+(\\w)+( HTTP\\/1.1)");
    // group 2,4,6,8 이 파라미터인 createUser 요청 Header 정규식
    private Pattern registrationHeader = Pattern.compile("(GET )(\\/registration)( HTTP\\/1.1)");
    private Pattern createUserHeader = Pattern.compile("(GET \\/create\\?userId\\=)+(\\w)+(&password=)+(\\w)+(&name=)+.*(&email=).*( HTTP\\/1.1)");

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

            if(getFileHeader.matcher(requestHeader).matches()){
                responseGetFile(dos, request);
            }
            else if(registrationHeader.matcher(requestHeader).matches()){
                responseRegistration(dos);
            }
            else if(createUserHeader.matcher(requestHeader).matches()){
                // create User 메서드
                log.info(requestHeader);
            }
            else {
                log.error(requestHeader);
            }


        } catch (IOException |  ArrayIndexOutOfBoundsException e) { // 리퀘스트 읽을 때 발생 예외
            log.error(e.getMessage());
        }
    }

    private void responseRegistration(DataOutputStream dos) throws IOException {
        byte[] body = responseBodyFile("/registration/index.html");
        response200Header(dos, body.length , FileType.HTML.getContentType());
        responseBody(dos,body);
    }

    private void responseGetFile(DataOutputStream dos, String[] request) throws IOException {
        // 리퀘스트를 파싱해 타입, 요청한 파일 경로 , 파일 확장자 얻음
        String requestType = request[0];
        String url = request[1];
        String fileType = url.split("\\.")[1];
        log.info(requestType + " : " + url);

        try {
            byte[] body = responseBodyFile(url);
            response200Header(dos, body.length, FileType.valueOf(fileType.toUpperCase()).getContentType());
            responseBody(dos, body);
        }catch (IOException noSuchFile){ // 해당 경로의 파일이 없을때
            response404(dos);
            log.error(noSuchFile.getMessage());
        }
    }

    private byte[] responseBodyFile(String url) throws IOException {
        File file = new File("src/main/resources/static" + url);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        return bytes;
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
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
        dos.writeBytes("\r\n" );
        dos.writeBytes(errorMessage); // body
    }
}