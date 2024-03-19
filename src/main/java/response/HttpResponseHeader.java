package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseHeader.class);
    private static final String VERSION = "HTTP/1.1";
    private static final String ESCAPE_SEQUENCE = "\r\n";
    private static final String SPACE = " ";
    private DataOutputStream dos;

    public HttpResponseHeader(DataOutputStream dos){
        this.dos = dos;
    }

    public void response200(String contentType, int contentLength){ // statusCode 200에 맞는 ResponseHeader 작성
        setStartLine("200", "OK");
        setContentType(contentType);
        setContentLength(contentLength);
    }

    public void response302(String location, String contentType, int contentLength){ // statusCode 302에 맞는 ResponseHeader 작성
        setStartLine("302", "FOUND");
        setLocation(location);
        setContentType(contentType);
        setContentLength(contentLength);
    }

    public void response404(String contentType, int contentLength){ // statusCode 404에 맞는 ResponseHeader 작성
        setStartLine("404", "Not Found");
        setContentType(contentType);
        setContentLength(contentLength);
    }

    private void setStartLine(String statusCode, String statusMessage){
        try {
            dos.writeBytes(VERSION + SPACE + statusCode + SPACE +statusMessage + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setLocation(String location) {
        try {
            dos.writeBytes("Location:" + SPACE + location + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setContentType(String contentType) {
        try {
            dos.writeBytes("Content-Type:" + SPACE + contentType + ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setContentLength(int contentLength) {
        try {
            dos.writeBytes("Content-Length:" + SPACE + contentLength + ESCAPE_SEQUENCE);
            dos.writeBytes(ESCAPE_SEQUENCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
