package webserver.Request;

import java.util.Map;
import java.util.StringJoiner;

public class Request {

    private final RequestStartLine startLine;
    private final RequestHeader header;
    private final String body;

    public Request(String startLine , Map<String, String> header , String body) {
        this.startLine = new RequestStartLine(startLine);
        this.header = new RequestHeader(header);
        this.body = body;
    }

    public Request(String startLine){
        this.startLine = new RequestStartLine(startLine);
        this.header = null;
        this.body = null;
    }

    public String getMethod(){
        return startLine.method;
    }

    public String getPath(){
        return startLine.path;
    }

    public Map<String, String> getQuery() {
        return startLine.query;
    }

    public String getVersion() {
        return startLine.version;
    }

    public String getLog() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(startLine.method.toUpperCase());
        sj.add(":");
        sj.add(startLine.path);

        return sj.toString();
    }
}
