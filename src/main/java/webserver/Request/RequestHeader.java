package webserver.Request;

import java.util.Map;

public class RequestHeader {
    Map<String , String> headerFields;
    RequestHeader(Map<String ,String> headerFields){
        this.headerFields = headerFields;
    }
}
