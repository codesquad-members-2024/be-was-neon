package webserver.HttpMessage;

import webserver.eums.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MessageBody {
    private final byte[] body;
    private final FileType contentType;

    public MessageBody(String body , FileType contentType){
        this.body = body.getBytes();
        this.contentType = contentType;
    }

    public MessageBody(File file) throws IOException{
        this.body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        }
        String[] fileName = file.getName().split("\\.");
        this.contentType = FileType.valueOf(fileName[fileName.length-1].toUpperCase());
    }
    public byte[] getBody() {
        return body;
    }
    public long getContentLength() {
        return body.length;
    }
    public FileType getContentType() {
        return contentType;
    }
    public String toString(){
        return new String(body);
    }
}
