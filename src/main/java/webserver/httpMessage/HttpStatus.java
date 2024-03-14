package webserver.httpMessage;

public enum HttpStatus {
    OK("HTTP/1.1 200 OK \r\n"),
    PERMANENT_REDIRECT("HTTP/1.1 308 Permanent Redirect \r\n");

    private final String status_message;

    HttpStatus(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus_message() {
        return status_message;
    }
}
