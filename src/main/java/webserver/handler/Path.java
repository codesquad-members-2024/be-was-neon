package webserver.handler;

public enum Path {
    STATIC_PATH("src/main/resources/static"),
    INDEX_HTML("/index.html"),
    REGISTRATION("/registration"),
    LOGIN("/login");

    final String path;

    Path(String s) {
        this.path = s;
    }
}
