package webserver.HttpMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static webserver.HttpMessage.constants.WebServerConst.QUERY_VALUE_DELIM;
import static webserver.HttpMessage.constants.WebServerConst.STARTLINE_DELIM;

public class RequestStartLine {
    private final String method;
    private final String uri;
    private Map<String, String> query;
    private final String version;

    public RequestStartLine(String startLine) {
        String[] splitLine = startLine.split(STARTLINE_DELIM);
        method = splitLine[0];
        uri = splitLine[1];
        version = splitLine[2];
        setQuery();
    }

    private void setQuery() {
        Pattern paramPattern = Pattern.compile("[\\?\\&]?\\w+=\\w+(%40\\w+\\.com)?");
        Matcher matcher = paramPattern.matcher(uri);
        Map<String, String> createParameters = new HashMap<>();

        while (matcher.find()) {
            String[] param = matcher.group().substring(1).split(QUERY_VALUE_DELIM);
            createParameters.put(param[0], param[1]); // '=' 제거
        }
        query = createParameters;
    }

    public Map<String, String> getQuery(){
        return Collections.unmodifiableMap(query);
    }

    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        return sj.add(method)
                .add(uri)
                .add(version).toString();
    }

    public String getMethod() {
        return this.method;
    }

    public String getUri(){
        return this.uri;
    }

    public String getVersion(){
        return this.version;
    }
}
