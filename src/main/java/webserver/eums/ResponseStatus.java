package webserver.eums;


public enum ResponseStatus {
    OK("200 OK"),
    NotFound("404 Not Found"),
    FOUND("302 Found");

    private final String message;

    ResponseStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

