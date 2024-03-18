package utils;

public class DirectoryMatcher {
    private static final String BASE_PATH = "src/main/resources/static";
    private static final String ARTICLE_PATH = "/article";
    private static final String ARTICLE_HTML = "/article.html";
    private static final String COMMENT_PATH = "/comment";
    private static final String COMMENT_HTML = "/comment.html";
    private static final String REGISTRATION_PATH = "/registration";
    private static final String REGISTRATION_HTML = "/register.html";
    private static final String LOGIN_PATH = "/login";
    private static final String LOGIN_HTML = "/login.html";

    public static String mathDirectory(String filePath) {
        if (filePath.equals(ARTICLE_PATH) || filePath.equals(ARTICLE_HTML)) {
            return BASE_PATH + ARTICLE_PATH + filePath;
        }
        if (filePath.equals(COMMENT_PATH) || filePath.equals(COMMENT_HTML)) {
            return BASE_PATH + COMMENT_PATH + filePath;
        }
        if (filePath.equals(REGISTRATION_PATH) || filePath.equals(REGISTRATION_HTML)) {
            return BASE_PATH + REGISTRATION_PATH + filePath;
        }
        if (filePath.equals(LOGIN_PATH) || filePath.equals(LOGIN_HTML)) {
            return BASE_PATH + LOGIN_PATH + filePath;
        }
        return BASE_PATH + filePath; // 폴더 안에 생성된 파일이 아닌 경우
    }
}
