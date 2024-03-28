package webserver.utils;


import webserver.ContentType;
import webserver.http.request.HttpRequest;
import webserver.http.request.message.HttpRequestStartLine;

import java.util.Arrays;


public class HttpRequestUtils {
    private static final String SLASH = "/";
    private static final String DEFAULT_FILE = "index.html";
    private static final String SPACE = " ";

    public static String getExtension(String requestLine) {
        return requestLine.substring(requestLine.lastIndexOf(".") + 1);
    }

    public static String getMimeType(String extension) {
        return Arrays.stream(ContentType.values())
                .filter(type -> type.name().equalsIgnoreCase(extension))
                .findFirst()
                .map(ContentType::getMimeType)
                .orElse("application/octet-stream"); // 못읽을 시, 기본값 설정
    }

    public static HttpRequest convertToStaticFileRequest(HttpRequest httpRequest) {
        String staticRequestLine = httpRequest.getRequestLine() + SLASH + DEFAULT_FILE;
        HttpRequestStartLine startLine = new HttpRequestStartLine(httpRequest.getMethod() + SPACE + staticRequestLine + SPACE + httpRequest.getHttpVersion());
        return new HttpRequest(startLine, httpRequest.getRequestHeader(), httpRequest.getRequestBody());
    }

    /* ---------------------------------------------------------- */


}
