package Utils;

import java.util.HashMap;
import java.util.Map;

public class RedirectManager {
    private static final Map<String, String> redirectMap = new HashMap<>();

    static {
        // 리다이렉션 규칙 초기화
        // 추가적인 리다이렉션 규칙은 여기에 등록
        redirectMap.put("/create", "/index.html");
    }

    public static String getRedirectUrl(String requestPath) {
        return redirectMap.getOrDefault(requestPath, null);
    }
}
