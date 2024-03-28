package Utils;

import Redirect.RequestProcessor;
import webserver.HttpRequest;
import webserver.HttpResponseWriter;

import java.io.IOException;

// 파일 시스템에 있는 정적 자원을 처리하는 클래스
public class StaticFileProcessor implements RequestProcessor {

    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    /**
     * 요청 URL에 따른 파일의 전체 경로를 반환합니다.
     *
     * @param requestURL 클라이언트로부터 받은 요청 URL
     * @return 해당 요청에 대응하는 파일의 경로
     */
    public static String getFilePath(String requestURL) {
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);

        // 기본 요청 처리
        completePath.append(requestURL);
        // 파일 확장자가 없는 경우 .html 추가
        if (!requestURL.contains(".")) {
            completePath.append(".html");
        }
        return completePath.toString();
    }

    public void handleFilerequest(HttpRequest request, HttpResponseWriter responseWriter) throws IOException {
        String filePath = getFilePath(request.getPath()); // 요청된 경로에 대응하는 실제 파일 경로 가져오기
        String contentType = ContentTypeManager.getContentType(filePath); // MIME 타입 결정
        responseWriter.sendResponse(ReadFile.readFileContent(filePath), contentType); // 클라이언트에게 파일 데이터와 함께 응답
    }
}