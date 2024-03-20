package utils;

public enum ContentType {
    HTML("text/html;charset=utf-8", ".html"),
    CSS("text/css", ".css"),
    JAVASCRIPT("text/javascript", ".js"),
    PNG("image/png", ".png"),
    JPG("image/jpeg", ".jpg"),
    ICO("image/x-icon", ".ico"),
    SVG("image/svg+xml", ".svg"),
    OCTET_STREAM("application/octet-stream", "");

    private final String contentType;
    private final String extension;

    ContentType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static String getContentTypeForExtension(String extension) {
        for (ContentType type : ContentType.values()) {
            if (type.extension.equals(extension)) {
                return type.contentType;
            }
        }
        return OCTET_STREAM.contentType; // 기본값인 application/octet-stream을 반환
    }
}
