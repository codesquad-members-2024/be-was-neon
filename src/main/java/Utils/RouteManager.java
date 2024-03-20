package Utils;

public class RouteManager {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";

    public static String makePath(String requestURL) {
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        switch (requestURL) {
            case "/registration":
                completePath.append("/registration").append(INDEX_FILE_NAME);
                break;
            case "/login":
                completePath.append("/login").append(INDEX_FILE_NAME);
                break;
            default:
                if (requestURL.equals("/")) {
                    completePath.append(INDEX_FILE_NAME);
                } else {
                    completePath.append(requestURL);
                    if (!requestURL.contains(".")) {
                        completePath.append(".html"); // 기본적으로 .html 추가
                    } else {
                        String extension = requestURL.substring(requestURL.lastIndexOf("."));
                        switch (extension) {
                            case ".css":
                            case ".js":
                            case ".ico":
                            case ".png":
                            case ".jpg":
                            case ".svg":
                                break;
                            default:
                                //TODO 지원하지 않는 확장자 로직
                                break;
                        }
                    }
                }
                break;
        }
        return completePath.toString();
    }
}
