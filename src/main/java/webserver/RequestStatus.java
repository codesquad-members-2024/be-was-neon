package webserver;

public enum RequestStatus {
    OK("200 OK") ,
    NotFound("404 Not Found") ,
    MethodNotAllowed("405 Method Not Allowed");


    private final String message;

    RequestStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
