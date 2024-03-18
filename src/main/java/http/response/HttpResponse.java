package http.response;

import http.Version;
import http.Version.ProtocolVersion;

public class HttpResponse {
    private Version version;
    private Status status;
    private Integer contentLength;
    private ContentType contentType;
    private byte[] httpRequestBody;

    public void setOkResponse(ContentType contentType, byte[] httpRequestBody) {
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = Status.OK;
        this.contentLength = httpRequestBody.length;
        this.contentType = contentType;
        this.httpRequestBody = httpRequestBody;
    }

    public void setErrorResponse(Status status) {
        byte[] httpRequestBody = String.format("<h1>%s</h1>", status.getMsg()).getBytes();
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = status;
        this.contentLength = httpRequestBody.length;
        this.contentType = ContentType.html;
        this.httpRequestBody = httpRequestBody;
    }

    public String getFormattedFirstLine() {
        return String.format("%s %s %s", version, status.getCode(), status.getMsg());
    }

    public String getFormattedContentType() {
        return String.format("Content-Type: %s", contentType.getContentType());
    }

    public String getFormattedContentLength() {
        return String.format("Content-Length: %s", contentLength.intValue());
    }

    public byte[] getHttpRequestBody() {
        return httpRequestBody;
    }
}