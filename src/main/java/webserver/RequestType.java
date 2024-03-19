package webserver;

public enum RequestType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private final String contentType;
    private final String MIMEType;

    RequestType(String contentType, String MIMEType) {
        this.contentType = contentType;
        this.MIMEType = MIMEType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMIMEType() {
        return MIMEType;
    }
}
