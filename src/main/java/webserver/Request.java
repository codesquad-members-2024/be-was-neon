package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private final String method;
    private String path;
    private Map<String, String> params;

    public Request(String method, String url) {
        this.method = method;
        this.path = url;
        setParams();
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    private void setParams() {
        Pattern paramPattern = Pattern.compile("[\\?\\&]?\\w+=\\w+(%40\\w+\\.com)?");
        Matcher matcher = paramPattern.matcher(path);
        Map<String, String> createParameters = new HashMap<>();

        while (matcher.find()) {
            String[] param = matcher.group().substring(1).split("=");
            createParameters.put(param[0], param[1]); // '=' 제거
        }
        params = createParameters;
    }

    public String getLog() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(method.toUpperCase());
        sj.add(":");
        sj.add(path);

        return sj.toString();
    }
}
