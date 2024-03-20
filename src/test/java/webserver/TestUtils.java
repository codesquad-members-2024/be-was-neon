package webserver;

import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.Request;
import webserver.eums.FileType;

class TestUtils {
    static final Request getIndexRequest = new Request("GET /index.html HTTP/1.1");
    static final Request getRegistrationRequest = new Request("GET /registration HTTP/1.1");
    static final Request createUserRequest = new Request("POST /create HTTP/1.1")
            .body(new MessageBody("userId=test&password=test&name=test&email=test%40naver", FileType.URLENCODED));
    static final Request loginRequest = new Request("POST /login HTTP/1.1")
            .body(new MessageBody("userId=test&password=test" , FileType.URLENCODED));

}
