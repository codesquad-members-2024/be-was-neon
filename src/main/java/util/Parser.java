package util;

public class Parser {
    public static int KEY = 0;
    public static int VALUE = 1;
    public static String[] splitColon(String tempRequest) {
        String[] headers = tempRequest.split(":", 2);
        String[] results = new String[headers.length];
        results[KEY] = headers[KEY].trim();
        results[VALUE] = headers[VALUE].trim();
        return results;
    }

    public static String getURI(String startRequest) {
        // GET /index.html HTTP/1.1 or POST /create?name=blah&password=blah
        String[] startLines = startRequest.split(" ");
        return startLines[1];
    }
}
