package webserver.requesthandler.http;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class HttpResponse {
    private HttpStatus status;
    private final Map<String, String> headers;
    private byte[] body;

    public HttpResponse() {
        this.headers = new HashMap<>();
        this.status = HttpStatus.OK;
        this.body = new byte[0];
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setRedirect(String url) {
        setHeader(HttpConst.HEADER_LOCATION, url);
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
        sj.add(cookie.getName() + HttpConst.PARAM_DELIMITER + cookie.getValue());
        if (cookie.getMaxAge() != -1) {
            sj.add("Max-Age=" + cookie.getMaxAge());
        }

        headers.put(HttpConst.HEADER_SET_COOKIE, sj.toString());
    }

    private void setContentType(ContentType contentType) {
        headers.put(HttpConst.HEADER_CONTENT_TYPE, contentType.getMimeType());
    }

    private void setContentLength(int length) {
        headers.put(HttpConst.HEADER_CONTENT_LENGTH, String.valueOf(length));
    }
}
