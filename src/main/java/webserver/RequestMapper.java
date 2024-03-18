package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

public class RequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(RequestMapper.class);

    private static final String STATIC_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    public static final String REGISTRATION = "/registration";
    public static final String LOGIN = "/login";

    private static final Map<String, Function<HttpRequest, HttpResponse>> map = Map.ofEntries(
            Map.entry("/", RequestMapper::home),
            Map.entry("/registration", RequestMapper::register),
            Map.entry("/login", RequestMapper::login),
            Map.entry("/create", RequestMapper::createUser)
    );

    public static HttpResponse service(HttpRequest request) {
        String uri = request.getUri();
        if (map.containsKey(uri)) {
            return map.get(uri).apply(request);
        }
        return getStaticFileResponse(uri);
    }

    private static HttpResponse getStaticFileResponse(String uri) {
        return HttpResponse.from(new File(STATIC_PATH + uri));
    }

    private static HttpResponse home(HttpRequest request) {
        File file = new File(STATIC_PATH + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private static HttpResponse register(HttpRequest request) {
        File file = new File(STATIC_PATH + REGISTRATION + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private static HttpResponse login(HttpRequest request) {
        File file = new File(STATIC_PATH + LOGIN + INDEX_HTML);
        return HttpResponse.from(file);
    }

    private static HttpResponse createUser(HttpRequest request) {
        Map<String, String> userForm = request.getBody();

        User user = User.from(userForm);
        Database.addUser(user);
        logger.debug("User created : {}", Database.findUserById(user.getUserId()));
        return HttpResponse.redirect(INDEX_HTML);
    }
}
