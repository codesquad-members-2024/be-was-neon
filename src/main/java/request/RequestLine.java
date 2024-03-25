package request;

import enums.ContentType;

public class RequestLine {
    private final String requestMethod;
    private final String requestPath;
    private final String requestProtocol;
    private final String queryParam;
    private String mimeType;

    public RequestLine(String requestMethod, String requestPath,String queryParam, String requestProtocol){
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.queryParam = queryParam;
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

    public String getMethod() {
        return requestMethod;
    }

    public String getPath() {
        return requestPath;
    }
    public String getQueryParam() {
        return queryParam;
    }

    public String getProtocol() {
        return requestProtocol;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(requestMethod).append(" ").append(requestPath);
        if (!queryParam.isEmpty()){
            sb.append("?").append(queryParam);
        }
        sb.append(" ").append(requestProtocol);
        return sb.toString();
    }
}
