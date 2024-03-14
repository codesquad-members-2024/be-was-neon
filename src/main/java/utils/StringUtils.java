package utils;

import webserver.RequestHandler;

public class StringUtils {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";

    public static String separatePath(String requestStartLine){ // request method에서 경로만 분리하기
        String[] splitStartLine = requestStartLine.split(" ");
        return splitStartLine[1];
    }

    public static String makeCompletePath(String requestURL){
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);
        if(!requestURL.equals(INDEX_FILE_NAME) && !requestURL.contains(RequestHandler.REGISTER_ACTION)){
            completePath.append(requestURL);
        }
        completePath.append(INDEX_FILE_NAME);
        return completePath.toString();
    }

}
