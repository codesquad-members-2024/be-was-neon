package response;

import enums.StatusCode;

public class ResponseLine {
    private String version;
    private StatusCode statusCode;

    public ResponseLine(String version, int statusCodeNum) {
        this.version = version;
        setStatusCode(statusCodeNum);
    }

    private void setStatusCode(int statusCodeNum) {
        for (StatusCode value : StatusCode.values()) {
            if (statusCodeNum == value.getStatusCode())
                this.statusCode = value;
        }
    }
    @Override
    public String toString() {
        return version + " " + statusCode.toString() + " \r\n";
    }
}
