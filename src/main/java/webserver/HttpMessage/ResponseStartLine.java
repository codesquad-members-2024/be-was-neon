package webserver.HttpMessage;

import webserver.eums.ResponseStatus;

public class ResponseStartLine {
    private final String version;
    private final ResponseStatus status;

    public ResponseStartLine(String version , ResponseStatus status) {
        this.version = version;
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String toString(){
        return version + " " + status.getMessage();
    }

}
