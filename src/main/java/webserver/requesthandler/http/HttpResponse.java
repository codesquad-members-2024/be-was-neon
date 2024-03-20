package webserver.requesthandler.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class HttpResponse {
    private final OutputStream out;
    private HttpStatus status;
    private final Map<String, String> headers;
    private byte[] body;

    public HttpResponse(OutputStream out) {
        this.out = out;
        this.status = HttpStatus.OK;
        this.headers = new HashMap<>();
        this.body = new byte[0];
    }

    public HttpResponse() {
        this.out = null;
        this.headers = new HashMap<>();
        this.status = HttpStatus.OK;
        this.body = new byte[0];
    }

    public void send() throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes(
                HttpConst.HTTP_VERSION + status.getValue() + HttpConst.START_LINE_DELIMITER + status.getReasonPhrase()
                        + HttpConst.CRLF);
        for (String name : headers.keySet()) {
            dos.writeBytes(name + HttpConst.HEADER_DELIMITER + headers.get(name) + HttpConst.CRLF);
        }
        dos.writeBytes(HttpConst.CRLF);
        if (body.length > 0) {
            dos.write(body);
        }
        dos.flush();
    }

    public void sendRedirect(String url) throws IOException {
        setRedirect(url);
        send();
    }

    public void setRedirect(String url) {
        setHeader("Location", url);
        setStatus(HttpStatus.FOUND);
    }

    public void setNotFound() {
        setStatus(HttpStatus.NOT_FOUND);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body, ContentType contentType) {
        this.body = body;
        setContentType(contentType);
        setContentLength(body.length);
    }

    public void setCookie(HttpCookie cookie) {
        StringJoiner sj = new StringJoiner(HttpConst.COOKIE_VALUE_DELIMITER);
        sj.add(cookie.getName() + "=" + cookie.getValue());
        if (cookie.getMaxAge() != -1) {
            sj.add("Max-Age=" + cookie.getMaxAge());
        }

        headers.put("Set-Cookie", sj.toString());
    }

    private void setContentType(ContentType contentType) {
        headers.put(HttpConst.CONTENT_TYPE_LABEL, contentType.getMimeType());
    }

    private void setContentLength(int length) {
        headers.put(HttpConst.CONTENT_LENGTH_LABEL, String.valueOf(length));
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public byte[] getBody() {
        return body;
    }
}
