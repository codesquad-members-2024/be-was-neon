package webserver.http.request.message;

public class HttpRequestBody {
    private String body;

    public HttpRequestBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
