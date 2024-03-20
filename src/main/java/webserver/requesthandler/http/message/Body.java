package webserver.requesthandler.http.message;

public class Body {
    private byte[] content;

    public Body(byte[] content) {
        this.content = content;
    }

    public Body() {
        content = new byte[0];
    }

    public byte[] getContent() {
        return content;
    }

    public String getStringContent() {
        return new String(content);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
