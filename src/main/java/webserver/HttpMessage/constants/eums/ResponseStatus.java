package webserver.HttpMessage.constants.eums;


public enum ResponseStatus {
    OK("200 OK"),

    // Redirection
    FOUND("302 Found"),

    // Error
    NotFound("404 Not Found"),
    MethodNotAllowed("405 Method Not Allowed");


    private final String message;

    ResponseStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

