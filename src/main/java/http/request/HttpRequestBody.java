package http.request;

import static utils.Constant.EMPTY;

public class HttpRequestBody {
    private static final String REPLACE_ID = "userId|userPw|userName|userEmail|=";
    private static final String SPLIT_REGEX = "&";

    private final String body;

    public HttpRequestBody(String body) {
        this.body = body;
    }


    public String[] extractUser() {
        String bodyValue = body.replaceAll(REPLACE_ID, EMPTY);
        return bodyValue.split(SPLIT_REGEX); // id, pw, name, email만 추출
    }
}
