package webserver.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestStartLine {
    final String method;
    public String version;
    String path;
    Map<String, String> query;

    RequestStartLine(String startLine) {
        String [] splitLine = startLine.split(" ");
        method = splitLine[0];
        path = splitLine[1];
        version = splitLine[2];
        setQuery();
    }

    private void setQuery() {
        Pattern paramPattern = Pattern.compile("[\\?\\&]?\\w+=\\w+(%40\\w+\\.com)?");
        Matcher matcher = paramPattern.matcher(path);
        Map<String, String> createParameters = new HashMap<>();

        while (matcher.find()) {
            String[] param = matcher.group().substring(1).split("=");
            createParameters.put(param[0], param[1]); // '=' 제거
        }
        query = createParameters;
    }
}
