package utils;

public class StringUtils {
    public static String separatePath(String requestStartLine){ // request method에서 경로만 분리하기
        String[] splitStartLine = requestStartLine.split(" ");
        return splitStartLine[1];
    }
}
