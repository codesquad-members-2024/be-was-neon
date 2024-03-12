package utils;

public class DirectoryMatcher {
    private static final String BASE_PATH = "src/main/resources/static";
    private static final String ARTICLE_PATH = "/article";
    private static final String COMMENT_PATH = "/comment";
    private static final String LOGIN_PATH = "/login";
    private static final String REGISTRATION_PATH = "/registration";

    public static String mathDirectory(String url) {
        String filePath = BASE_PATH;
        if (url.equals("/article.html")) {
            return filePath + ARTICLE_PATH + url;
        }
        if (url.equals("/comment.html")) {
            return filePath + COMMENT_PATH + url;
        }
        if (url.equals("/register.html")) {
            return filePath + REGISTRATION_PATH + url;
        }
        if (url.equals("/login.html")) {
            return filePath + LOGIN_PATH + url;
        }
        return filePath + url; // 폴더 안에 생성된 파일이 아닌 경우
    }
}
