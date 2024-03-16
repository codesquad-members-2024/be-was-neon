package webserver.HttpMessage;

import java.util.StringJoiner;

public class Response {
    private final ResponseStartLine startLine;
    private MessageHeader header;
    private MessageBody body;

    public Response(ResponseStartLine startLine) {
        this.startLine = startLine;
    }

    public Response header(MessageHeader messageHeader){
        this.header =messageHeader;
        return this;
    }

    public Response body(MessageBody body){
        this.body = body;
        return this;
    }

    public ResponseStartLine getStartLine() {
        return startLine;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        if(body == null) return null;
        return body.getBody();
    }

    public String toString() {
        StringJoiner sj = new StringJoiner("\r\n");
        sj.add(startLine.toString())
                .add(header.toString() + "\r\n");
        return sj.toString();
    }
}
