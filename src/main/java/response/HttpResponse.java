package response;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private ResponseLine responseLine;
    private List<String> responseHeaders = new ArrayList<>();
    private byte[] body = null;

    public void sendResponse(DataOutputStream dos){
        try{
            dos.writeBytes(responseLine.toString());
            writeHeaders(dos);
            if (body != null){
                dos.write(body,0,body.length);
            }
            dos.flush();

        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void setResponseLine(String version, int statusCode){
        this.responseLine = new ResponseLine(version, statusCode);
    }
    public void addHeader(String header){
        responseHeaders.add(header);
    }
    private void writeHeaders(DataOutputStream out) {
        try {
            for (String header: responseHeaders) {
                out.writeBytes(header + "\r\n");
            }
            out.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public void setBody(byte[] body){
        this.body = body;
    }
}
