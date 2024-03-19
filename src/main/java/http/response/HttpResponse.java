package http.response;

import static utils.Constant.BASIC_ROUTE;
import static utils.Constant.CRLF;
import static utils.Constant.ERROR_MSG_FORMAT;

import http.Version;
import http.Version.ProtocolVersion;

public class HttpResponse {
    private Version version;
    private Status status;
    private Integer contentLength;
    private ContentType contentType;
    private String location;
    private byte[] httpRequestBody;

    public void setOkResponse(ContentType contentType, byte[] httpRequestBody) {
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = Status.OK;
        this.contentLength = httpRequestBody.length;
        this.contentType = contentType;
        this.httpRequestBody = httpRequestBody;
    }

    public void setRedirectResponse() {
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = Status.REDIRECT;
        this.location = BASIC_ROUTE;
    }

    public void setErrorResponse(Status status) {
        byte[] httpRequestBody = String.format(ERROR_MSG_FORMAT, status.getMsg()).getBytes();
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = status;
        this.contentLength = httpRequestBody.length;
        this.contentType = ContentType.html;
        this.httpRequestBody = httpRequestBody;
    }

    public byte[] getHttpRequestBody() {
        return httpRequestBody;
    }

    public String getFormattedResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s %s", version.getVersion(), status.getCode(), status.getMsg())).append(CRLF);
        if (status.equals(Status.REDIRECT)) {
            sb.append(String.format("Location: %s", location)).append(CRLF);
            return sb.toString();
        }
        sb.append(String.format("Content-Type: %s", contentType.getContentType())).append(CRLF);
        sb.append(String.format("Content-Length: %s", contentLength)).append(CRLF);
        return sb.toString();
    }
}