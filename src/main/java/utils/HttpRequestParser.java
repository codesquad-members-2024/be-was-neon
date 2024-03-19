package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpRequestParser {
    REQUEST_LINE(Pattern.compile("(^GET|^POST) (/.*) (HTTP/.{1,3})")),
    HEADERS(Pattern.compile("([^:\\s]+):\\s?(.+)\\s")),
    QUERY_PARAMETER(Pattern.compile("([^?&=\\s]+)=([^&\\s]+)")),
    ;

    private final Pattern compiledPattern;

    HttpRequestParser(Pattern compiledPattern) {
        this.compiledPattern = compiledPattern;
    }

    public static String parseRequestLine(String header) {
        Matcher requestLineMatcher = REQUEST_LINE.compiledPattern.matcher(header);
        if (requestLineMatcher.find()) {
            return requestLineMatcher.group();
        }
        return "";
    }

    public static Map<String, String> parseHeader(String header) {
        Map<String, String> headers = new HashMap<>();

        Matcher headerMatcher = HEADERS.compiledPattern.matcher(header);
        while (headerMatcher.find()) {
            headers.put(headerMatcher.group(1), headerMatcher.group(2));
        }
        return Collections.unmodifiableMap(headers);
    }

    public static Map<String, String> parseParams(String header) {
        Map<String, String> paramMap = new HashMap<>();

        Matcher paramMatcher = QUERY_PARAMETER.compiledPattern.matcher(header);
        while (paramMatcher.find()) {
            paramMap.put(paramMatcher.group(1), paramMatcher.group(2)); // key = 인덱스 1, value = 인덱스 2
        }
        return Collections.unmodifiableMap(paramMap);
    }
}
