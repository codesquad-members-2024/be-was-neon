package webserver.handler.requesthandler;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpResponseUtils;

import java.io.*;

public class DynamicFileHandler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicFileHandler.class);

    private HttpRequest httpRequest;

    public DynamicFileHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse handleRequest(String replacement, String pattern) {
        try {
            byte[] responseBody = getDynamicFileContent(replacement, pattern).getBytes();
            String header = HttpResponseUtils.generateStaticResponseHeader(httpRequest, responseBody.length);
            String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.OK);
            return new HttpResponse(startLine, header, responseBody);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get404Response();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get500Response();
        }
    }

    private String getDynamicFileContent(String replacement, String pattern) {
        String staticContent = getStaticFileContent(httpRequest);
        return staticContent.replaceFirst(pattern, replacement);
    }

    private String getStaticFileContent(HttpRequest httpRequest) {
        File file = new File(Config.getStaticRoute() + httpRequest.getRequestLine());

        // 404 error
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("[404 ERROR] Request 파일이 존재하지 않음 : " + file.getAbsolutePath());
        }

        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            String line;
            // 한 줄씩 읽어서 StringBuilder에 추가
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("[500 ERROR] 읽는 도중 문제 발생 ");
        }
    }


}
