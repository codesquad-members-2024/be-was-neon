package utils;

public class StringUtils {
    public static String separatePath(String requestStartLine){
        String[] splitStartLine = requestStartLine.split(" ");
        return splitStartLine[1];
    }
}
