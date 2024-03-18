package webserver.httpMessage;

public enum HttpStatus {
    OK("HTTP/1.1 200 OK \r\n"),
    TEMPORARY_REDIRECTION("HTTP/1.1 307 Temporary Redirect \r\n"),
    SEE_OTHER("HTTP/1.1 303 See Other \r\n");

    private final String statusMessage;

    HttpStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
