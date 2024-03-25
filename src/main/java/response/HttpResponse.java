package response;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import userservice.UserHandler;

import java.io.*;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final int SESSION_COOKIE_MAX_AGE = 100;

    public void respondToHtmlFile(DataOutputStream out, String filePath, String fileType) {
        try {
            byte[] fileBytes = HttpFileHandler.htmlToByte(filePath);
            sendResponse(out, "HTTP/1.1 200 OK", fileBytes.length, fileType, "", "", fileBytes);
        } catch (Exception e) {
            logger.error("Failed to respond with HTML file: " + filePath, e);
        }
    }

    public void handleRegistrationRequest(DataOutputStream out, String requestBody, String redirectURL) {
        try {
            Map<String, String> userData = UserHandler.extractUserData(requestBody);
            User user = UserHandler.createUserFromData(userData);
            UserHandler.addUserToDataBase(user);
            sendRedirect(out, redirectURL, "");
        } catch (Exception e) {
            logger.error("Failed to handle registration request", e);
        }
    }

    public void handleLoginRequest(DataOutputStream out, String sessionId, String redirectURL) {
        try {
            sendRedirect(out, redirectURL, sessionId);
        } catch (Exception e) {
            logger.error("Failed to handle login request", e);
        }
    }

    private void sendResponse(DataOutputStream out, String statusLine, int contentLength, String contentType, String location, String sessionId, byte[] body) {
        try {
            out.writeBytes(statusLine + "\r\n");
            addResponseHeader(out, "Content-Type", contentType + ";charset=utf-8");
            addResponseHeader(out, "Content-Length", String.valueOf(contentLength));
            addSessionCookie(out, sessionId);
            addResponseHeader(out, "Location", location);
            out.writeBytes("\r\n");
            if (body != null) {
                out.write(body, 0, body.length);
            }
            out.flush();
        } catch (IOException e) {
            logger.error("Failed to send HTTP response", e);
        }
    }

    private void sendRedirect(DataOutputStream out, String location, String sessionId) {
        sendResponse(out, "HTTP/1.1 302 Found", -1, "", location, sessionId, null);
    }

    private void addResponseHeader(DataOutputStream out, String headerName, String headerValue) throws IOException {
        if (headerValue != null && !headerValue.isEmpty()) {
            out.writeBytes(headerName + ": " + headerValue + "\r\n");
        }
    }

    private void addSessionCookie(DataOutputStream out, String sessionId) throws IOException {
        if (sessionId != null && !sessionId.isEmpty()) {
            out.writeBytes("Set-Cookie: SID=" + sessionId + "; Max-Age=" + SESSION_COOKIE_MAX_AGE + "; Path=/\r\n");
        }
    }
}
