package FileUtils;

public class FileExtractor {
    private static final String BLANK = "\s+";
    private static final int URL_INDEX = 1;

    public static String extractUrl(String filePath) {
        return filePath.split(BLANK)[URL_INDEX]; // html Url 추출
    }
}
