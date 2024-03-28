package webserver;

import java.io.File;

public class UrlConvertor {
    private final static String BASE_URL = "src/main/resources/static";
    public final static String INDEX_URL = "/index.html";
    public final static String LOGIN_FAIL_URL = "/login/login_failed.html";

    public String urlController(String requestTarget) {
        // 기본 경로 추가
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(requestTarget);

        File file = new File(sb.toString());

        // 디렉토리면 index.html 추가
        if (file.isDirectory()) {
            sb.append(INDEX_URL);
        }
        return sb.toString();
    }

    public String typeController(String requestTarget) {
        return ContentType.getType(requestTarget);
    }
}
