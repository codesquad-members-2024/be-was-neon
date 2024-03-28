package Redirect;

import Utils.StaticFileProcessor;

import java.util.HashMap;
import java.util.Map;

// 경로에 따라 적절한 RequestProcessor를 선택하는 역할을 하는 클래스
public class RedirectSelector {
    private static final Map<String, RequestProcessor> routeMap = new HashMap<>();

    static {
        routeMap.put("/create", new CreateProcessor());
        routeMap.put("/login", new LoginProcessor());
        routeMap.put("/registration", new RegistProcessor());
        routeMap.put("/", new StaticFileProcessor());
    }

    // 요청된 경로에 대응하는 RequestProcessor를 반환
    public static RequestProcessor getProcessor(String path) {
        return routeMap.getOrDefault(path, new StaticFileProcessor());
    }
}
