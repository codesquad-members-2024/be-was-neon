package webserver.Mapping;

import webserver.HttpHandler.RequestHandler;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.RequestStartLine;
import webserver.HttpMessage.Response;

import java.lang.reflect.Method;
import java.util.function.BiPredicate;

public class MappingMatcher {
    private final Request request;
    private final RequestStartLine startLine;
    private final RequestHandler handler;

    public MappingMatcher(Request request) {
        this.request = request;
        this.startLine = request.getStartLine();
        handler = new RequestHandler();
    }

    public Response getResponse() throws Exception {
        final String httpMethod = startLine.getMethod();
        final String path = startLine.getUri().split("\\?")[0];

        if (httpMethod.equals("GET")) {
            return handleRequest(path, handler, this::matchGetMapping);
        }
        if (httpMethod.equals("POST")) {
            return handleRequest(path, handler, this::matchPostMapping);
        } else throw new IllegalAccessException("설정되어 있지 않은 http 메소드입니다.");
    }

    private Response handleRequest(String uri, RequestHandler handler, BiPredicate<Method, String> matchMapping) throws Exception {
        for (Method method : RequestHandler.class.getDeclaredMethods()) {
            if (matchMapping.test(method, uri)) {
                return (Response) method.invoke(handler, request);
            }
        }
        // default : get Resource
        return handleRequest("/" , handler , matchMapping);
    }

    private boolean matchGetMapping(Method method, String uri) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            String path = method.getAnnotation(GetMapping.class).path();
            return path.equals(uri);
        }
        return false;
    }

    private boolean matchPostMapping(Method method, String uri) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            String path = method.getAnnotation(GetMapping.class).path();
            return path.equals(uri);
        }
        return false;
    }
}
