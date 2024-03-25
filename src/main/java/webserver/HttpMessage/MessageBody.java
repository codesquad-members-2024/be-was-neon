package webserver.HttpMessage;

import webserver.HttpMessage.constants.eums.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static webserver.HttpMessage.constants.WebServerConst.*;

public class MessageBody {
    private final byte[] body;
    private final FileType contentType;

    /**
     * Key - Value 형식의 내용일 경우 HashMap 에 저장
     */
    private final Map<String, String> content = new HashMap<>();

    /**
     * @param body HTTP Message body 문자열
     * @param contentType body 내용 유형
     */
    public MessageBody(String body, FileType contentType) {
        this.body = body.getBytes();
        this.contentType = contentType;

        if (contentType == FileType.URLENCODED) {
            // tokenizer
            StringTokenizer st = new StringTokenizer(body , QUERY_START+QUERY_DELIM);

            while (st.hasMoreTokens()){
                String[] token = st.nextToken().split(QUERY_VALUE_DELIM);
                content.put(token[0], token[1]);
            }
        }
    }

    public MessageBody(File file) throws IOException {
        this.body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        }
        String[] fileName = file.getName().split(EXTENDER_START);
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
