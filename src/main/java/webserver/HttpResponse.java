package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String MAIN_PAGE_URL = "/index.html";

    public void respondHtmlFile (DataOutputStream dos, String fileName, String fileType){
        try{
            response200Header(dos, Objects.requireNonNull(HttpFileHandler.htmlToByte(fileName)).length, fileType);
            responseBody(dos, HttpFileHandler.htmlToByte(fileName));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void handleRegistrationRequest(DataOutputStream dos, String postBody){
        try{
            Map<String, String> paramMap = RegistrationHandler.extractUserData(postBody);
            UserHandler.createUserFromData(paramMap);
            response302Redirect(dos, MAIN_PAGE_URL);
        } catch (Exception e) {
            logger.error("Error handling registration request: {}", e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String fileType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileType + ";charset=utf-8\r\n");
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
    private void response302Redirect(DataOutputStream dos, String location){
        // 302 response 는 get 으로 유저 데이터를 받아온후에 redirect 하여 클라이언트를 로그인 페이지로 연결시켜줍니다.
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
            dos.flush();
        }catch (IOException e) {
            logger.error("Error sending 302 redirect response: {}", e.getMessage());
        }
    }
}
