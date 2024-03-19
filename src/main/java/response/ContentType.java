package response;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JS("application/javascript"),
    SVG("image/svg+xml"),
    ICO("image/x-icon"),
    JPEG("image/jpeg"),
    JPG("image/jpeg"),
    PNG("image/png");
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static String getContentType(String inputType) { // 파일 type에 맞는 ContentType을 반환
        return valueOf(inputType.toUpperCase()).type;
    }
}
