package webserver;

import webserver.httpMessage.HttpResponse;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

public class RequestMapper {

    private static final String STATIC_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";

    private final Map<String, Supplier<HttpResponse>> map = Map.ofEntries(
            Map.entry("/", this::home),
            Map.entry("/registration", this::register),
            Map.entry("/login", this::login)
    );

    public HttpResponse getHttpResponse(String requestTarget) {
        if (requestTarget.contains(".")) {
            return HttpResponse.from(new File(STATIC_PATH + requestTarget));
        }
        return map.get(requestTarget).get();
    }

    private HttpResponse home() {
        File file = new File(STATIC_PATH + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private HttpResponse register() {
        File file = new File(STATIC_PATH + "/registration" + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private HttpResponse login() {
        File file = new File(STATIC_PATH + "/login" + INDEX_HTML);
        return HttpResponse.from(file);
    }
}
