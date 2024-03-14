package http;

public enum ContentType {
    text("text/plain"),
    html("text/html"),
    css("text/css"),
    js("application/javascript"),
    ico("image/x-icon"),
    png("image/png"),
    jpg("image/jpeg"),
    svg("image/svg+xml");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
