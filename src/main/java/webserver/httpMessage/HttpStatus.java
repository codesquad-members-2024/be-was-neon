package webserver.httpMessage;

public enum HttpStatus {
    OK("HTTP/1.1 200 OK \r\n"),
    PERMANENT_REDIRECT("HTTP/1.1 308 Permanent Redirect \r\n");

    private final String statusMessage;

    HttpStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
