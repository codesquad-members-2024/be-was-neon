package webserver;

import java.io.*;
import java.net.Socket;

import httpMessage.HttpRequest;
import httpMessage.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;
import util.RequestLineParser;

public class RequestHandler implements Runnable {
    private static final String DEFAULT_PATH = "./src/main/resources/static";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) { // 소켓 타입의 인자를 받아 connection 필드에 저장
        this.connection = connectionSocket;
    }

    public void run() {
        // 클라이언트 연결 정보 로깅 : 클라이언트가 연결되면, IP 주소와 포트 번호를 로깅한다.
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);

            // 첫 번째 라인에서 요청 URL 추츨 (/register.html)
            HttpRequest httpRequest = new HttpRequest(in);
            String line = httpRequest.getRequestLine();
            logger.debug("request line : {}", line);
            RequestLineParser requestLineParser = new RequestLineParser(line);
            String url = requestLineParser.getRequestURL();

            // header 출력
            httpRequest.printHeaders(httpRequest.getHeaders());

            // 모든 정적 리소스를 공통된 방식으로 처리
            String filePath = DEFAULT_PATH + url;


            HttpResponse httpResponse = new HttpResponse(dos);

            // 여기서 부터는 회원 가입 로직 처리
            // 📌 만약에 path 가 create 로 시작하면 (회원 가입 버튼 누르면)
            if (url.startsWith("/create")) {
                // 파싱 한 정보를 User 에 넘긴다
                User user = new User(requestLineParser.getValue("userId"), requestLineParser.getValue("nickName"), requestLineParser.getValue("password"));
                // 그리고 다시 register.html 로 돌아간다 -> 200 아니고 302 응답
                httpResponse.response302(dos);
                return;
            }

            byte[] body = getHtml(filePath).getBytes();
            String contentType = getContentType(filePath);

            httpResponse.setBody(body);
            httpResponse.response200Header(dos, body.length, contentType);
            httpResponse.responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void  processRequest(HttpRequest httpRequest, HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        String url = httpRequest.getRequestURL();
        if (url.startsWith("/create")) {
            processSignUp(httpRequest, httpResponse, dos);
        } else {
            serveStaticResource(url, httpResponse, dos);
        }
    }

    private void processSignUp(HttpRequest httpRequest, HttpResponse httpResponse, DataOutputStream dos) {
        User user = new User(httpRequest.getValue("userId"), httpRequest.getValue("nickName"), httpRequest.getValue("password"));
        // 그리고 다시 register.html 로 돌아간다 -> 200 아니고 302 응답
        httpResponse.response302(dos);
    }

    private void serveStaticResource(String url, HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        // 모든 정적 리소스를 공통된 방식으로 처리
        String filePath = DEFAULT_PATH + url;
        byte[] body = getHtml(filePath).getBytes();
        String contentType = getContentType(filePath);

        httpResponse.setBody(body);
        httpResponse.response200Header(dos, body.length, contentType);
        httpResponse.responseBody(dos, body);
    }

    private String getHtml(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                sb.append(currentLine);
            }
        } catch (IOException e) {
            throw new IOException("file not found : " + path);
        }
        return sb.toString();
    }

    // 파일 확장자에 따라 적절한 Content-Type을 반환한다
    private String getContentType(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex != -1) {
            String extension = filePath.substring(dotIndex + 1);
            return ContentType.findByExtension(extension).getMimeType();
        }
        return ContentType.DEFAULT.getMimeType();
    }
}
