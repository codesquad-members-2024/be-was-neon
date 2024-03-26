package Utils;

/**
 * 서버의 요청 URL에 따라 적절한 파일 경로와 컨텐츠 타입을 반환하는 유틸리티 클래스
 */
public class RouteManager {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";

    /**
     * 요청 URL에 따른 파일의 전체 경로를 반환합니다.
     *
     * @param requestURL 클라이언트로부터 받은 요청 URL
     * @return 해당 요청에 대응하는 파일의 경로
     */
    public static String getFilePath(String requestURL) {
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        // 루트 요청 처리
        if ("/".equals(requestURL)) {
            return completePath.append(INDEX_FILE_NAME).toString();
        }

        // 특정 경로 요청 처리
        if ("/registration".equals(requestURL) || "/login".equals(requestURL)) {
            return completePath.append(requestURL).append(INDEX_FILE_NAME).toString();
        }

        // 기본 요청 처리
        completePath.append(requestURL);
        // 파일 확장자가 없는 경우 .html 추가
        if (!requestURL.contains(".")) {
            completePath.append(".html");
        }
        return completePath.toString();
    }

    /**
     * 파일 경로를 기반으로 적절한 컨텐츠 타입을 반환
     *
     * @param filePath 파일 경로
     * @return 파일의 확장자에 따른 컨텐츠 타입
     */
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

    /**
     * 파일 경로로부터 파일의 확장자를 추출
     *
     * @param filePath 파일 경로
     * @return 파일의 확장자. 확장자가 없는 경우 빈 문자열 반환
     */
    private static String getFileExtension(String filePath) {
        int lastIndexOfDot = filePath.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return filePath.substring(lastIndexOfDot).toLowerCase();
        }
        return ""; // 확장자가 없는 경우
    }
}
