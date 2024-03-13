package utils;

public class FileExtractor {
    private static final String BLANK = "\s+";
    private static final int URL_INDEX = 1;
    private static final String EMPTY = "";
    private static final String REPLACE_PATH = "/\\w+/create\\?";
    private static final String REPLACE_ID = "userId|userPw|userName|userEmail|=";
    private static final String SPLIT_REGEX = "&";

    public static String[] extract(String request) {
        return request.split(BLANK);
    }

    public static String extractUrl(String filePath) {
        return filePath.split(BLANK)[URL_INDEX]; // html Url 추출
    }

    public static String[] extractUser(String url) {
        url = url.replaceAll(REPLACE_PATH, EMPTY);
        url = url.replaceAll(REPLACE_ID, EMPTY);
        return url.split(SPLIT_REGEX); // id, pw, name, email만 추출
    }
}
