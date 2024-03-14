package webserver;

public enum ContentType {
    html("text/html;charset=utf-8"),
    css("text/css;charset=utf-8"),
    js("application/javascript"),
    svg("image/svg+xml"),
    ico("image/x-icon"),
    jpeg("image/jpeg"),
    jpg("image/jpeg"),
    png("image/png");
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static String getContentType(String inputType) {
        return valueOf(inputType).type;
    }
}
