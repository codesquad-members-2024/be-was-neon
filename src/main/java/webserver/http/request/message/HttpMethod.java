package webserver.http.request.message;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private String methodDescription;

    HttpMethod(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public String getMethodDescription() {
        return methodDescription;
    }

    /* 현재 사용하지 않는 Method들에 대한 주석 처리
     * HEAD,
     * PUT,
     * DELTE,
     * CONNECT,
     * OPTIONS,
     * TRAGE,
     * PATCH
     */

}
