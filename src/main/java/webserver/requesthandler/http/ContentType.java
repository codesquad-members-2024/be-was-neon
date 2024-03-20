package webserver.requesthandler.http;

import java.util.Arrays;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static ContentType getContentTypeByExtension(String fileExtension) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.extension.equals(fileExtension))
                .findAny()
                .orElse(HTML);
    }
}

