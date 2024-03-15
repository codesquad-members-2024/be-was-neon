package webserver.httpMessage;

public enum HttpStatus {
    OK("HTTP/1.1 200 OK \r\n"),
    TEMPORARY_REDIRECTION("HTTP/1.1 307 Temporary Redirect \r\n");

    private final String statusMessage;

    HttpStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
