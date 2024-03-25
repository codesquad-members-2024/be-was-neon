package Utils;

public class RouteManager {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";

    public static String getFilePath(String requestURL) {
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        if ("/".equals(requestURL)) {
            return completePath.append(INDEX_FILE_NAME).toString();
        }

        if ("/registration".equals(requestURL) || "/login".equals(requestURL)) {
            return completePath.append(requestURL).append(INDEX_FILE_NAME).toString();
        }

        completePath.append(requestURL);
        if (!requestURL.contains(".")) {
            completePath.append(".html"); // 파일 확장자가 없는 경우 .html 추가
        }
        return completePath.toString();
    }

    public static String getContentType(String filePath) {
        String extension = getFileExtension(filePath);

        return switch (extension) {
            case ".css" -> "text/css;charset=utf-8";
            case ".js" -> "application/javascript;charset=utf-8";
            case ".png" -> "image/png";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".ico" -> "image/x-icon";
            case ".svg" -> "image/svg+xml";
            case ".html" -> "text/html;charset=utf-8";
            default -> "application/octet-stream"; // 일치하는 확장자가 없는 경우
        };
    }

    private static String getFileExtension(String filePath) {
        String extension = "";
        int lastIndexOfDot = filePath.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            extension = filePath.substring(lastIndexOfDot).toLowerCase();
        }
        return extension;
    }
}
