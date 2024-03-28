package webserver;

import java.util.Arrays;

public enum ContentType {
    CSS("text/css", ".css"),
    JS("application/javascript", ".js"),
    PNG("image/png", ".png"),
    JPG("image/jpeg", ".jpg"),
    ICO("image/x-icon", ".ico"),
    SVG("image/svg+xml", ".svg"),
    HTML("text/html", ".html");

    private final String type;
    private final String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public static String getType(String requestTarget) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> requestTarget.endsWith(contentType.extension))
                .findFirst()
                .map(contentType -> contentType.type)
                .orElse(HTML.type);
    }
}
