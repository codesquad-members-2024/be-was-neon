package http.response;

public enum ContentType {
    txt("text/plain;charset=utf-8"),
    html("text/html;charset=utf-8"),
    css("text/css;charset=utf-8"),
    js("application/javascript;charset=utf-8"),
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
