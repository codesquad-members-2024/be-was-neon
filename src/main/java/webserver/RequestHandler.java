package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    public static final String REGISTER_ACTION = "/user/create";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            HttpRequest httpRequest = new HttpRequest(br);

            // String requestLine = br.readLine();
            logger.debug("request method : {}", httpRequest.getStartLine());

            if(httpRequest.checkRegisterDataEnter()){
                httpRequest.parseRegisterData();
                httpRequest.storeDatabase();
            }

            // 요청 받은 URL을 파싱하여 파일 경로를 결정한다.
            String filePath = httpRequest.getCompletePath();

//            requestLine = br.readLine();
//            while(!requestLine.isEmpty()){ // 나머지 header 출력
//                logger.debug("request header : {}", requestLine);
//                requestLine = br.readLine();
//            }

            // 파일이 존재하면 해당 파일을 읽어 응답.
            byte[] fileContent;
            String statusCode;
            String statusMessage;
            String contentType;

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                fileContent = fis.readAllBytes();
                fis.close();

                statusCode = "200";
                statusMessage = "OK";
                contentType = "Content-Type: text/html;charset=utf-8";

                /*response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);*/
            } else {
                // 파일이 존재하지 않으면 404 응답.
                fileContent = "<h1>404 Not Found</h1>".getBytes();

                statusCode = "404";
                statusMessage = "Not Found";
                contentType = "Content-Type: text/html;charset=utf-8";
            }
            HttpResponseHeader httpResponseHeader = new HttpResponseHeader(dos);
            HttpResponseBody httpResponseBody = new HttpResponseBody(dos);

            httpResponseHeader.setStartLine(statusCode, statusMessage);
            httpResponseHeader.setContentType(contentType);
            httpResponseHeader.setContentLength(fileContent.length);

            httpResponseBody.setBody(fileContent);
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void response302Header(DataOutputStream dos){ // 회원가입 정보 받고 redirect에 사용
//        try {
//            dos.writeBytes("HTTP/1.1 302 FOUND\r\n");
//            dos.writeBytes("Location: " + "/index.html" + "\r\n"); // redirect 경로 지정
//            dos.writeBytes("\r\n");
//            dos.flush();
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
}