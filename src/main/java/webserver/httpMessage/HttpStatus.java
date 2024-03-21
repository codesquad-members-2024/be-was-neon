package webserver.httpMessage;

public enum HttpStatus {
    OK("HTTP/1.1 200 OK"),
    TEMPORARY_REDIRECTION("HTTP/1.1 307 Temporary Redirect"),
    SEE_OTHER("HTTP/1.1 303 See Other");

    private final String statusMessage;

    HttpStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
