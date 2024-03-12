package Utils;

/**
 * 요청된 URL을 파싱하여 다양한 정보를 추출하는 클래스
 */
public class PathParser {

    /**
     * 요청 라인에서 요청 URL을 추출하는 메소드
     *
     * @param requestLine HTTP 요청의 첫 번째 라인입니다. 예: "GET /index.html HTTP/1.1"
     * @return 요청된 URL의 경로를 반환합니다. 예: "/index.html"
     */
    public static String extractPathFromRequestLine(String requestLine) {

        String[] tokens = requestLine.split(" ");

        if (tokens.length < 3) {
            throw new IllegalArgumentException("잘못된 요청 라인 형식입니다.");
        }

        /**
         * 첫번쨰 문자열의 요소는 ex: GET 클라이언트의 요청
         * 두번째 문자열의 요소는 ex: /index.html 경로
         * 세번째 문자열의 요소는 ex: HTTP/1.1 HTTP 프로토콜의 버전
        */
        return tokens[1];
    }
}
