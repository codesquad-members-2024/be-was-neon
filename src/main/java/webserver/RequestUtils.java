package webserver;

public class RequestUtils {
    public static String extractRequestURL(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length >= 2) {
            return tokens[1]; // 요청 URL
        } else {
            return null;
        }
    }
}
