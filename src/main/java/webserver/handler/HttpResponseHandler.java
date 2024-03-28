package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseHandler.class);

    private DataOutputStream dos;

    public HttpResponseHandler(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void handleResponse(HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStartLine());
            dos.writeBytes(httpResponse.getResponseHeader());
            dos.write(httpResponse.getResponseBody(), 0, httpResponse.getResponseBody().length);

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
