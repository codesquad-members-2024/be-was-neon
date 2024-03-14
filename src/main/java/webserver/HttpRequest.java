package webserver;

import java.io.*;

public class HttpRequest {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";

    private String startLine;
    private String method;
    private String url;
    private String version;


    HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        this.startLine = br.readLine();
        parseStartLine(startLine);
    }

    private void parseStartLine(String startLine){
        String[] splitStartLine = startLine.split(" ");
        this.method = splitStartLine[0];
        this.url = splitStartLine[1];
        this.version = splitStartLine[2];
    }

    public String getCompletePath(){
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);
        if(!url.equals(INDEX_FILE_NAME) && !url.contains(RequestHandler.REGISTER_ACTION)){
            completePath.append(url);
        }
        completePath.append(INDEX_FILE_NAME);
        return completePath.toString();
    }

    public String getStartLine(){
        return startLine;
    }

    public String getMethod(){
        return method;
    }

    public String getUrl(){
        return url;
    }

    public String getVersion(){
        return version;
    }
}
