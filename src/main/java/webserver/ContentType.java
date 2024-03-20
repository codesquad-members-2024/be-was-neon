package webserver;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
