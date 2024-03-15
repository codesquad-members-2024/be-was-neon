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
    private final String fileType;

    ContentType(String fileType) {
        this.fileType = fileType;
    }

    public static String getContentType(String inputType) {
        return valueOf(inputType).fileType;
    }
}
