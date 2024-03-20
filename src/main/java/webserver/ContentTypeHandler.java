package webserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import static webserver.ResponseHandler.*;

import static webserver.RequestHandler.INDEX_FILE_PATH;

public class ContentTypeHandler {
    public void handleContent(String requestLine, OutputStream out) throws IOException {
        String extension = extractExtension(requestLine);
        ContentType contentType = getContentType(extension);

        if (contentType != null) {
            File file = new File(INDEX_FILE_PATH , requestLine);
            if (file.isDirectory()) {
                file = new File(file, "/index.html");
            }
            if (file.exists()) {
                sendResponseContentType(out, file, contentType);
            } else {
                response404(out);
            }
        } else {
            // 지원되지 않는 확장자인 경우 404 응답 전송
            response404(out);
        }
    }


    private ContentType getContentType(String extension) {
        for (ContentType type : ContentType.values()) {
            if (extension.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }

    private String extractExtension(String requestLine) {
        int dotIndex = requestLine.lastIndexOf(".");
        if (dotIndex != -1) {
            return requestLine.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }
}


