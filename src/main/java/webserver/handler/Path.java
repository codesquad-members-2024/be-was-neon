package webserver.handler;

public enum Path {
    RESOURCE_PATH("src/main/resources/static"),
    HOME_PAGE("/index.html"),
    REGISTRATION_PAGE("/registration/index.html"),
    LOGIN_PAGE("/login/index.html"),
    LOGIN_FAILED_PAGE("/login/login_failed.html");

    private static final String staticPath = "src/main/resources/static";
    private final String path;

    Path(String s) {
        this.path = s;
    }
    
    public String getRelativePath() {
        return staticPath + path; 
    }

    public String getPath() {
        return path;
    }
}
