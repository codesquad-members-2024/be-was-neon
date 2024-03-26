package enums;

public enum StatusCode {
    OK(200, "OK"),
    FOUND(302, "Found"),
    NOT_FOUND(404, "Not Found");
    //SERVER_ERROR(500, "");
    private int statusCode;
    private String statusMsg;

    StatusCode(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public int getStatusCode(){
        return statusCode;
    }
    public String getStatusMsg(){
        return statusMsg;
    }

    @Override
    public String toString() {
        return statusCode + " " + statusMsg;
    }
}
