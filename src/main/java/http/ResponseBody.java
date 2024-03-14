package http;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseBody {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBody.class);
    private final DataOutputStream dos;

    public ResponseBody(DataOutputStream dos) {
        this.dos = dos;
    }

    public void createBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
