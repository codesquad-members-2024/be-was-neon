package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;

import static enums.FilePath.*;

public class GetResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetResponseHandler.class);
    private final HttpResponse httpResponse = new HttpResponse();

    public HttpResponse respondToHtmlFile(HttpRequest httpRequest) {
        try {
            httpResponse.setResponseLine(httpRequest.getProtocol(),200);
            byte[] fileBytes = HttpFileReader.htmlToByte(STATIC_PATH.getPath() + httpRequest.getPath());
            httpResponse.addHeader("Content-Type: " + httpRequest.getMimeType() + ";charset=utf-8");
            httpResponse.addHeader("Content-Length: " + fileBytes.length);
            httpResponse.setBody(fileBytes);
            return httpResponse;
        } catch (Exception e) {
            logger.error("Failed to respond with HTML file: " + e);
            return response404Error(httpRequest);
        }
    }
    public HttpResponse respondToLogin(HttpRequest httpRequest, String sessionId, String redirectPath) {
        try {
            httpResponse.setResponseLine(httpRequest.getProtocol(),302);
            httpResponse.addHeader("Set-Cookie: SID=" + sessionId + "; Path=/");
            httpResponse.addHeader("Location: " + redirectPath);
            return httpResponse;
        } catch (Exception e) {
            logger.error("Failed to send login respond: " + e);
            return response404Error(httpRequest);
        }
    }
    private HttpResponse response404Error(HttpRequest httpRequest) {
        HttpResponse errorResponse = new HttpResponse();
        errorResponse.setResponseLine(httpRequest.getProtocol(), 404);
        return errorResponse;
    }
}
