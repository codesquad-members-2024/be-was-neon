package http.response;

public enum Status {
    OK("200", "OK"),
    CREATED("201", "Created"),
    NO_CONTENT("204", "No Content"),
    REDIRECT("302", "Found"),
    NOT_MODIFIED("304", "Not Modified"),
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    SERVICE_UNAVAILABLE("503", "Service Unavailable");

    private final String code;
    private final String msg;

    Status(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
