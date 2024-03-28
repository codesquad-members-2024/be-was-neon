package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;

public class PostResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetResponseHandler.class);
    private final HttpResponse httpResponse = new HttpResponse();

    public HttpResponse respondToRegistration(HttpRequest httpRequest, String redirectPath) {
        try {
            httpResponse.setResponseLine(httpRequest.getProtocol(),302);
            httpResponse.addHeader("Location: " + redirectPath);
            return httpResponse;
        } catch (Exception e) {
            logger.error("Failed to send registration respond: " + e);
            return response404Error(httpRequest);
        }
    }
    public HttpResponse respondToLogout(HttpRequest httpRequest, String redirectPath) {
        try {
            httpResponse.setResponseLine(httpRequest.getProtocol(),302);
            httpResponse.addHeader("Location: " + redirectPath);
            httpResponse.addHeader("Set-Cookie: SID=;Max-Age=0; Path=/");
            //같은 key 값의 쿠키를 생성하고 바로 만료 시킨다.
            return httpResponse;
        } catch (Exception e) {
            logger.error("Failed to send logout respond: " + e);
            return response404Error(httpRequest);
        }
    }

    private HttpResponse response404Error(HttpRequest httpRequest) {
        HttpResponse errorResponse = new HttpResponse();
        errorResponse.setResponseLine(httpRequest.getProtocol(), 404);
        return errorResponse;
    }
}
