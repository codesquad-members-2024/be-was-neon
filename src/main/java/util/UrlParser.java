package util;

public class UrlParser {


    public static String parseRequest(String input) {
        String[] requestSplits = input.replaceAll("\\/", "").split(" ");
        return requestSplits[1].trim();
        //requestSplits[1]=요청 URL
    }

}
