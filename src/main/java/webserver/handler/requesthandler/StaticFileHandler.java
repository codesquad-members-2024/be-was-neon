package webserver.handler.requesthandler;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.util.Arrays;

public class StaticFileHandler implements HttpRequestHandler {
    private static final String DEFAULT_FILE = "/index.html";
    private static final Logger logger = LoggerFactory.getLogger(StaticFileHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        try {
            return generateStaticFileResponse(httpRequest);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get404Response(); // 파일을 못찾으면 404 처리
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get500Response(); // 서버 오류 500 처리
        }
    }

    private HttpResponse generateStaticFileResponse(HttpRequest httpRequest) {
        validateMimeType(httpRequest.getRequestLine());

        // 2) 파일 읽기 -> 없다면 IllegalArgumentException [404]
        //            -> 읽다가 예외 발생 시 RuntimeException [500]
        byte[] responseBody = HttpResponseUtils.generateStaticResponseBody(httpRequest);

        // 3) header 작성하기
        String header = HttpResponseUtils.generateStaticResponseHeader(httpRequest, responseBody.length);

        // 4) startLine 작성하기
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.OK);

        // 5) response 객체 리턴
        return new HttpResponse(startLine, header, responseBody);
    }

    private void validateMimeType(String requestLine) {
        String extension = HttpRequestUtils.getExtension(requestLine);

        if (!isValidExtension(extension)) {
            throw new IllegalArgumentException("[ERROR] 지원하지 않는 Mime Type : " + extension);
        }
    }

    private boolean isValidExtension(String extension) {
        return Arrays.stream(ContentType.values())
                .anyMatch(contentType -> contentType.name().equals(extension)); // 정의된 확장자 중에 있을 때는 true!
    }


}
