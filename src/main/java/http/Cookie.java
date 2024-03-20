package http;

import static utils.HttpConstant.EQUAL;
import static utils.HttpConstant.SP;
import static utils.HttpConstant.SPLITTER;

public class Cookie {

    private final String cookieKey;
    private String cookieValue;

    public Cookie(String cookieKey, String cookieValue) {
        this.cookieKey = cookieKey;
        this.cookieValue = cookieValue + SPLITTER + SP;
    }

    public void setPath(String path) {
        cookieValue += "Path=" + path + SPLITTER + SP;
    }

    public String getCookie() {
        return cookieKey + EQUAL + cookieValue;
    }

    public String getCookieKey() {
        return cookieKey;
    }

    public String getCookieValue() {
        return cookieValue;
    }
}
