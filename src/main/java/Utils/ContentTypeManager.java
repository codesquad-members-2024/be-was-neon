package Utils;

/**
 * 서버의 요청 URL에 따라 적절한 파일 경로와 컨텐츠 타입을 반환하는 유틸리티 클래스
 */
public class ContentTypeManager {
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
