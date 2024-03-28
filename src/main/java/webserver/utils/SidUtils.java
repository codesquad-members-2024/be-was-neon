package webserver.utils;

public class SidUtils {
    private final static String SID_START_LINE = "sid=";

    public static String getCookieSid(String cookie) {
        if (cookie == null) {
            throw new IllegalArgumentException("[ERROR] 쿠키가 없습니다.");
        }

        int startIndex = cookie.indexOf(SID_START_LINE);

        if (startIndex == -1) {
            throw new IllegalArgumentException("[ERROR] Cookie의 Sid가 없습니다");
        }

        startIndex += SID_START_LINE.length();

        return cookie.substring(startIndex);
    }
}
