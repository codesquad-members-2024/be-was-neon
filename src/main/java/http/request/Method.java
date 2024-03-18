package http.request;

import static utils.Constant.IS_INVALID_HTTP_METHOD;

import http.HttpRequestRouter;
import java.util.Arrays;
import java.util.function.Consumer;

public class Method {
    public enum HttpMethod {
        GET(HttpRequestRouter::processGETMethod),
        POST(HttpRequestRouter::processPOSTMethod),
        PUT(HttpRequestRouter::processPUTMethod),
        DELETE(HttpRequestRouter::processDELETEMethod),
        HEAD(HttpRequestRouter::processHEADMethod),
        CONNECT(HttpRequestRouter::processCONNECTMethod),
        TRACE(HttpRequestRouter::processTRACEMethod),
        PATCH(HttpRequestRouter::processPATCHMethod);

        private final Consumer<HttpRequestRouter> function;

        HttpMethod(Consumer<HttpRequestRouter> function) {
            this.function = function;
        }

        public void execute(HttpRequestRouter httpRequestRouter) {
            function.accept(httpRequestRouter);
        }
    }

    private final String method;

    public Method(String method) {
        if (!isValidMethod(method)) {
            throw new IllegalArgumentException(IS_INVALID_HTTP_METHOD);
        }
        this.method = method;
    }

    private boolean isValidMethod(String method) {
        return Arrays.stream(HttpMethod.values())
                .anyMatch(httpMethod -> httpMethod.name().equals(method.toUpperCase()));
    }

    public String getMethod() {
        return method;
    }
}
