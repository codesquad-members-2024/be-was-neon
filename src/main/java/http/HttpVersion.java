package http;

public enum HttpVersion {
    V_09("0.9"),
    V_10("1.0"),
    V_11("1.1"),
    V_20("2.0"),
    V_30("3.0");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
