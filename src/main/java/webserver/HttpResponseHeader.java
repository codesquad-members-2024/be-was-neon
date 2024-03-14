package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseHeader.class);
    private DataOutputStream dos;

    HttpResponseHeader(DataOutputStream dos){
        this.dos = dos;
    }

    public void setStartLine(String statusCode, String statusMessage){
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " +statusMessage + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setContentType(String contentType) {
        try {
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setLocation(String location) {
        try {
            dos.writeBytes("Location: " + location + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setContentLength(int contentLength) {
        try {
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
