package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpHeaderParser {
    REQUEST_LINE(Pattern.compile("(^GET|^POST) (/.*) (HTTP/.{1,3})")),
    HOST(Pattern.compile("Host:.*")),
    USER_AGENT(Pattern.compile("User-Agent:.*")),
    ACCEPT(Pattern.compile("Accept:.*")),
    ACCEPT_LANGUAGE(Pattern.compile("Accept-Language:.*")),
    ACCEPT_ENCODING(Pattern.compile("Accept-Encoding:.*")),
    REFERER(Pattern.compile("Referer:.*")),
    CONNECTION(Pattern.compile("Connection:.*")),
    IF_MODIFIED_SINCE(Pattern.compile("If-Modified-Since:.*")),
    IF_NONE_MATCH(Pattern.compile("If-None-Match:.*")),
    CACHE_CONTROL(Pattern.compile("Cache-Control:.*")),
    COOKIE(Pattern.compile("Cookie:.*")),
    PRAGMA(Pattern.compile("Pragma:.*")),
    OTHER(Pattern.compile(".*:.*")),
    ;

    private final Pattern compiledPattern;

    HttpHeaderParser(Pattern compiledPattern) {
        this.compiledPattern = compiledPattern;
    }

    public String parse(String header) {
        Matcher headerMatcher = compiledPattern.matcher(header);
        if (headerMatcher.find()) {
            return headerMatcher.group();
        }
        return "";
    }
}
