package webserver;

import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.constants.eums.FileType;

public class TestUtils {
    public static final Request getIndexRequest = new Request("GET /index.html HTTP/1.1");
    public static final Request getRegistrationRequest = new Request("GET /registration HTTP/1.1");
    public static final Request createUserRequest = new Request("POST /registration HTTP/1.1")
            .body(new MessageBody("userId=test&password=test&name=test&email=test%40naver.com", FileType.URLENCODED));

}
