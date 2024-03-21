package webserver;

import webserver.handler.*;
import webserver.httpMessage.HttpRequest;

import java.util.Map;

public class RequestMapper {

    private static final Map<String, Handler> map = Map.ofEntries(
            Map.entry("/", new HomeHandler()),
            Map.entry("/index.html", new HomeHandler()),
            Map.entry("/registration", new RegistrationHandler()),
            Map.entry("/create", new UserCreateHandler()),
            Map.entry("/login", new LoginHandler()),
            Map.entry("/user/list", new UserListHandler())
    );

    private static final StaticFileHandler fileHandler = new StaticFileHandler();

    public static Handler findHandler(HttpRequest request) {
        String uri = request.getUri();
        return map.getOrDefault(uri, fileHandler);
    }
}
