package request;

import enums.ContentType;

public class RequestLine {
    private final String requestMethod;
    private final String requestPath;
    private final String requestProtocol;
    private String mimeType;

    public RequestLine(String requestMethod, String requestPath, String requestProtocol){
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestProtocol = requestProtocol;
        setMimeType();
    }
    public void setMimeType() {
        for (ContentType contentType : ContentType.values()) {
            if (requestPath.contains(contentType.getExtension())) {
                this.mimeType = contentType.getMimeType();
            }
        }
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getRequestProtocol() {
        return requestProtocol;
    }

    public String getMimeType() {
        return mimeType;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(requestMethod)
                .append(" ")
                .append(requestPath)
                .append(" ")
                .append(requestProtocol);
        return sb.toString();
    }
}
