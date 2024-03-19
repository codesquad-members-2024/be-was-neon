package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseBody {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseBody.class);
    private DataOutputStream dos;

    public HttpResponseBody(DataOutputStream dos){
        this.dos = dos;
    }

    public void setBody(byte[] body){
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
