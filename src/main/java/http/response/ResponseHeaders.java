package http.response;

public class ResponseHeaders {
    private Integer contentLength;
    private ContentType contentType;
    private String location;
    private String cookie;

    public ResponseHeaders(Integer contentLength, ContentType contentType) {
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public ResponseHeaders(String location) {
        this.location = location;
    }

    public ResponseHeaders(ContentType contentType, String cookie) {
        this.contentType = contentType;
        this.cookie = cookie;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType.getContentType();
    }

    public String getLocation() {
        return location;
    }

    public String getCookie() {
        return cookie;
    }
}
