package webserver.http.response;

public class HttpResponse {
    private String startLine;
    private String responseHeader;
    private byte[] responseBody;

    public HttpResponse(String startLine, String responseHeader) {
        this.startLine = startLine;
        this.responseHeader = responseHeader;
        this.responseBody = new byte[0];
    }

    public HttpResponse(String startLine, String responseHeader, byte[] responseBody) {
        this.startLine = startLine;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public String getStartLine() {
        return startLine;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

}
