package webserver.requesthandler.http;

import java.net.HttpCookie;
import java.util.Map;
import java.util.StringJoiner;
import webserver.requesthandler.http.message.Body;
import webserver.requesthandler.http.message.Headers;

public class HttpResponse {
    private HttpStatus status;
    private Headers headers;
    private Body body;

    public HttpResponse() {
        this.status = HttpStatus.OK;
        this.headers = new Headers();
        this.body = new Body();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public byte[] getBody() {
        return body.getContent();
    }

    public Map<String, String> getHeaders() {
        return headers.getStore();
    }

    public String getCookieValues() {
        return getHeader(HttpConst.HEADER_SET_COOKIE);
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
        this.body.setContent(body);
        setContentType(contentType);
        setContentLength(body.length);
    }

    public void addCookie(HttpCookie cookie) {
        StringJoiner sj = new StringJoiner(HttpConst.COOKIE_VALUE_DELIMITER);
        sj.add(cookie.getName() + HttpConst.PARAM_DELIMITER + cookie.getValue());
        if (cookie.getMaxAge() != -1) {
            sj.add("Max-Age=" + cookie.getMaxAge()); // 만료 기한 존재시 설정
        }

        String original = headers.get(HttpConst.HEADER_SET_COOKIE);

        String modified;
        if (original != null) {
            modified = original + HttpConst.SET_COOKIE_DELIMITER + sj;
        } else {
            modified = sj.toString();
        }

        headers.put(HttpConst.HEADER_SET_COOKIE, modified);
    }

    private void setContentType(ContentType contentType) {
        headers.put(HttpConst.HEADER_CONTENT_TYPE, contentType.getMimeType());
    }

    private void setContentLength(int length) {
        headers.put(HttpConst.HEADER_CONTENT_LENGTH, String.valueOf(length));
    }
}
