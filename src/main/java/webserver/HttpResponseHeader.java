package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseHeader.class);
    private static final String ESCAPE_SEQUENCE = "\r\n";
    private DataOutputStream dos;

    HttpResponseHeader(DataOutputStream dos){
        this.dos = dos;
    }

    public void setStartLine(String statusCode, String statusMessage){
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " +statusMessage + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setContentType(String contentType) {
        try {
            dos.writeBytes("Content-Type: " + contentType + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setLocation(String location) {
        try {
            dos.writeBytes("Location: " + location + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setContentLength(int contentLength) {
        try {
            dos.writeBytes("Content-Length: " + contentLength + ESCAPE_SEQUENCE);
            dos.writeBytes(ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
