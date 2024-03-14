package webserver;

import db.Database;
import model.User;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.utils.Parser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.function.Function;

public class RequestMapper {

    private static final String STATIC_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    public static final String REGISTRATION = "/registration";
    public static final String LOGIN = "/login";

    private final Map<String, Function<HttpRequest, HttpResponse>> map = Map.ofEntries(
            Map.entry("/", this::home),
            Map.entry("/registration", this::register),
            Map.entry("/login", this::login),
            Map.entry("/create", this::createUser)
    );

    public HttpResponse service(HttpRequest request) {
        String uri = request.getUri();
        if (map.containsKey(uri)) {
            return map.get(uri).apply(request);
        }
        return HttpResponse.from(new File(STATIC_PATH + uri));
    }

    private HttpResponse home(HttpRequest request) {
        File file = new File(STATIC_PATH + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private HttpResponse register(HttpRequest request) {
        File file = new File(STATIC_PATH + REGISTRATION + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private HttpResponse login(HttpRequest request) {
        File file = new File(STATIC_PATH + LOGIN + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private HttpResponse createUser(HttpRequest request) {
        String queryParams = request.getQueryParams();
        try {
            Map<String, String> userForm = Parser.splitQuery(queryParams);
            Database.addUser(User.from(userForm));
            File file = new File(STATIC_PATH + LOGIN + INDEX_HTML);
            return HttpResponse.from(file);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
