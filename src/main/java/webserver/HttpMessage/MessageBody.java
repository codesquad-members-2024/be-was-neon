package webserver.HttpMessage;

import webserver.eums.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBody {
    private final byte[] body;
    private final FileType contentType;
    private final Map<String, String> content = new HashMap<>();

    public MessageBody(String body, FileType contentType) {
        this.body = body.getBytes();
        this.contentType = contentType;

        if (contentType == FileType.URLENCODED) {
            Pattern paramPattern = Pattern.compile("[\\?\\&]?\\w+=\\w+(%40\\w+\\.com)?");
            Matcher matcher = paramPattern.matcher("?" + body);

            while (matcher.find()) {
                String[] param = matcher.group().substring(1).split("=");
                content.put(param[0], param[1]);
            }
        }
    }

    public MessageBody(File file) throws IOException {
        this.body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        }
        String[] fileName = file.getName().split("\\.");
        this.contentType = FileType.valueOf(fileName[fileName.length - 1].toUpperCase());
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

    public String getContentByKey(String key) throws IllegalArgumentException{
        String value = content.get(key);

        if(value == null) throw new IllegalArgumentException("not exists key :" + key);
        return content.get(key);
    }

    public String toString() {
        return new String(body);
    }
}
