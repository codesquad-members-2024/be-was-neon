package http.response;

import static utils.Constant.BASIC_ROUTE;
import static utils.Constant.CRLF;
import static utils.Constant.ERROR_MSG_FORMAT;

import http.Version;
import http.Version.ProtocolVersion;

public class HttpResponse {
    private Version version;
    private Status status;
    private ResponseHeaders responseHeaders;
    private byte[] httpRequestBody;

    public void setOkResponse(ContentType contentType, byte[] httpRequestBody) {
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = Status.OK;
        responseHeaders = new ResponseHeaders(httpRequestBody.length, contentType);
        this.httpRequestBody = httpRequestBody;
    }

    public void setRedirectResponse() {
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = Status.REDIRECT;
        responseHeaders = new ResponseHeaders(BASIC_ROUTE);
    }

    public void setErrorResponse(Status status) {
        byte[] httpRequestBody = String.format(ERROR_MSG_FORMAT, status.getMsg()).getBytes();
        this.version = new Version(ProtocolVersion.V_11.getVersion());
        this.status = status;
        responseHeaders = new ResponseHeaders(httpRequestBody.length, ContentType.html);
        this.httpRequestBody = httpRequestBody;
    }

    public byte[] getHttpRequestBody() {
        return httpRequestBody;
    }

    public String getFormattedResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s %s", version.getVersion(), status.getCode(), status.getMsg())).append(CRLF);
        if (status.equals(Status.REDIRECT)) {
            sb.append(String.format("Location: %s", responseHeaders.getLocation())).append(CRLF);
            return sb.toString();
        }
        sb.append(String.format("Content-Type: %s", responseHeaders.getContentType())).append(CRLF);
        sb.append(String.format("Content-Length: %s", responseHeaders.getContentLength())).append(CRLF);
        return sb.toString();
    }
}