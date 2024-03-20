package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String MAIN_PAGE_URL = "/index.html";

    public void respondHtmlFile (DataOutputStream dos, String fileName, String fileType){
        try{
            send200Response(dos, HttpFileHandler.htmlToByte(fileName).length, fileType);
            responseBody(dos, HttpFileHandler.htmlToByte(fileName));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void handleRegistrationRequest(DataOutputStream dos, String postBody){
        try{
            Map<String, String> paramMap = RegistrationHandler.extractUserData(postBody);
            UserHandler.createUserFromData(paramMap);
            send302Redirect(dos, MAIN_PAGE_URL);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public static void send200Response(DataOutputStream dos, int lengthOfBodyContent, String fileType) {
        sendResponse(dos, "HTTP/1.1 200 OK", lengthOfBodyContent, fileType, "");
    }

    public static void send302Redirect(DataOutputStream dos, String location) {
        sendResponse(dos, "HTTP/1.1 302 Found", -1, "", "Location: " + location);
    }

    private static void sendResponse(DataOutputStream dos, String statusLine, int lengthOfBodyContent, String fileType, String additionalHeader) {
        try {
            dos.writeBytes(statusLine + "\r\n");
            dos.writeBytes("Content-Type: " + fileType + ";charset=utf-8\r\n");
            if (lengthOfBodyContent >= 0) {
                dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            }
            if (additionalHeader != null && !additionalHeader.isEmpty()) {
                dos.writeBytes(additionalHeader + "\r\n");
            }
            dos.writeBytes("\r\n");
            dos.flush();
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
