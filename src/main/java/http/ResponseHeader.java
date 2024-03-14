package http;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseHeader {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHeader.class);
    private final DataOutputStream dos;

    public ResponseHeader(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setStatus(HttpStatus httpStatus) {
        try {
            dos.writeBytes("HTTP/1.1" + " " + httpStatus.getCode() + " " + httpStatus.getDescription() + "\r\n");
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

    public void setLengthOfBodyContent(int lengthOfBodyContent) {
        try {
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
