package webserver.requesthandler.http;

import static webserver.requesthandler.http.HttpConst.CRLF;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponseWriter {
    private final OutputStream out;

    public HttpResponseWriter(OutputStream out) {
        this.out = out;
    }

    public void send(HttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes(HttpConst.HTTP_VERSION + response.getStatus().getValue() + HttpConst.START_LINE_DELIMITER
                + response.getStatus().getReasonPhrase() + CRLF);

        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            dos.writeBytes(header.getKey() + HttpConst.HEADER_DELIMITER + header.getValue() + CRLF);
        }
        dos.writeBytes(CRLF);

        if (response.getBody().length > 0) {
            dos.write(response.getBody());
        }
        dos.flush();
    }
}