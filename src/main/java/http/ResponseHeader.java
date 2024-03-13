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

    public void createHeader(int status, String description, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1" + " " + status + " " + description + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
