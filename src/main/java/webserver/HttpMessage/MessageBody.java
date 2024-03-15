package webserver.HttpMessage;

import webserver.eums.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MessageBody {
    private byte[] body;
    private FileType contentType;

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
//        System.out.println(fileName[fileName.length-1]);
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
