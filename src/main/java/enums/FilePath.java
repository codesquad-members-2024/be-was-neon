package enums;

public enum FilePath {
    STATIC_PATH("./src/main/resources/static"),
    HOME_PAGE("/index.html"),
    REGISTRATION_PAGE("/registration/index.html"),
    LOGIN_HOME_PAGE("/main/index.html"),
    LOGIN_PAGE("/login/index.html"),
    LOGIN_FAILED_PAGE("/login/login_failed.html");

    private static final String RELATIVE_PATH = "./src/main/resources/static";
    private final String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getPath() {
        return filePath;
    }
    public String getRelativePathPath() {
        return RELATIVE_PATH + filePath;
    }
}
