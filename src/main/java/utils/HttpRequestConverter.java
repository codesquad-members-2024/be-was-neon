package utils;

import static utils.HttpHeaderParser.*;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestConverter.class);
    private static final String NEWLINE = System.lineSeparator();
    private static final String BLANK = " ";
    private static final int METHOD_INDEX = 0;
    private static final int URL_INDEX = 1;
    private static final int HTTP_VERSION_INDEX = 2;

    public static HttpRequest convertToHttpRequest(Socket connection) {
        String header = makeOneLine(connection);

        return convert(header);
    }

    public static String makeOneLine(Socket connection) {
        StringBuilder builder = new StringBuilder();
        try (InputStream in = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                builder.append(line).append(NEWLINE); // request string를 한 줄씩 추가하고 마지막에 개행 문자 추가
            }
        } catch (IOException e) {
            logger.error("[REQUEST CONVERTER ERROR] {}", e.getMessage());
        }
        return builder.toString();
    }

    public static HttpRequest convert(String header) {
        HttpRequestBuilder builder = new HttpRequestBuilder();

        /* request line */
        String[] requestLine = getRequestLine(header);

        HttpMethod method = HttpMethod.valueOf(requestLine[METHOD_INDEX]);
        String requestURI = requestLine[URL_INDEX];
        String httpVersion = requestLine[HTTP_VERSION_INDEX];

        builder.setMethod(method);
        builder.setRequestURI(requestURI);
        builder.setHttpVersion(httpVersion);

        /* Host */
        builder.setHost(HOST.parse(header));

        /* User-Agent */
        builder.setUserAgent(USER_AGENT.parse(header));

        /* Connection */
        builder.setConnection(CONNECTION.parse(header));

        /* Accept */
        builder.setAccept(ACCEPT.parse(header));

        /* Accept-Encoding */
        builder.setAcceptEncoding(ACCEPT_ENCODING.parse(header));

        /* Accept-Language */
        builder.setAcceptLanguage(ACCEPT_LANGUAGE.parse(header));

        /* Referer */
        builder.setReferer(REFERER.parse(header));

        /* If-Modified-Since */
        builder.setIfModifiedSince(IF_MODIFIED_SINCE.parse(header));

        /* If-None-Match */
        builder.setIfNoneMatch(IF_NONE_MATCH.parse(header));

        /* Cache-Control */
        builder.setCacheControl(CACHE_CONTROL.parse(header));
        builder.setCookie(COOKIE.parse(header));
        builder.setPragma(PRAGMA.parse(header));

        return builder.build();
    }

    private static String[] getRequestLine(String header) {
        return REQUEST_LINE.parse(header).split(BLANK);
    }
}
