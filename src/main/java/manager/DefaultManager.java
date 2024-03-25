package manager;

import request.FileInfo;
import request.HttpRequest;
import response.ContentType;
import response.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DefaultManager {
    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public DefaultManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException {
        String completePath = FileInfo.makeCompletePath(httpRequest.getStartLineInfo("url"));
        String contextType = ContentType.getContentType(FileInfo.getFileType(completePath));

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("200", "OK");
        httpResponse.setContentType(contextType);
        httpResponse.setContentLength(String.valueOf(body.length));

        httpResponse.setBody(body);

        return httpResponse;
    }
}
