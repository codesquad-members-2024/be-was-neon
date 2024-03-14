package utils;

import http.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseConverter.class);
    public static HttpResponse convertToHttpResponse(Socket connection) {
        Optional<HttpResponse> response = Optional.empty();
        try {
            OutputStream out = connection.getOutputStream();
            response = Optional.of(new HttpResponse(new DataOutputStream(out)));
        } catch (IOException e) {
            logger.error("[RESPONSE CONVERTER ERROR] {}", e.getMessage());
        }
        return response.orElseThrow(() -> new IllegalStateException("[RESPONSE CONVERTER ERROR] convert fail"));
    }
}
