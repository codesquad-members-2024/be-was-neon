package response;

import request.FileInfo;
import request.RequestManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResponseManager {
    RequestManager requestManager;
    HttpResponseHeader httpResponseHeader;
    byte[] body;

    public ResponseManager(RequestManager requestManager){
        this.requestManager = requestManager;
        httpResponseHeader = new HttpResponseHeader();
    }

    public void setResponse() throws IOException {
        String completePath = requestManager.getCompletePath();
        String contextType = ContentType.getContentType(FileInfo.getFileType(completePath));

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        body = fis.readAllBytes();
        fis.close();

        if(requestManager.isMethodGet()){ // GET 인 경우
            httpResponseHeader.setStartLine("200", "OK");
            httpResponseHeader.setContentType(contextType);
            httpResponseHeader.setContentLength(String.valueOf(body.length));
        } else if(requestManager.isMethodPost() && requestManager.isUrlUserCreate()) { // POST, /user/create 인 경우
            httpResponseHeader.setStartLine("302", "FOUND");
            httpResponseHeader.setLocation("/index.html");
        }else { // GET POST 둘다 아닌 경우
            body = "<h1>404 Not Found</h1>".getBytes();
            httpResponseHeader.setStartLine("404", "Not Found");
            httpResponseHeader.setContentType(contextType);
            httpResponseHeader.setContentLength(String.valueOf(body.length));
        }
    }

    public String getResponseHeader(){
        StringBuilder buildResponse = new StringBuilder();

        buildResponse.append(httpResponseHeader.makeStartLine());
        if(httpResponseHeader.isLocationExist()){
            buildResponse.append(httpResponseHeader.makeLocation());
        }
        if(httpResponseHeader.isContentTypeExist()){
            buildResponse.append(httpResponseHeader.makeContentType());
        }
        if(httpResponseHeader.isContentLengthExist()){
            buildResponse.append(httpResponseHeader.makeContentLength());
        }
        return buildResponse.toString();
    }

    public void writeResponse(DataOutputStream dos) throws IOException {
        dos.writeBytes(getResponseHeader()); // StartLine + Headers
        dos.writeBytes(httpResponseHeader.makeEmptyLine()); // 공백
        dos.write(body, 0, body.length); // Body
    }
}
