package model;

import java.util.Map;

public class HttpBody {
    Map<String,String> keyValue;
    public HttpBody(Map<String,String> keyValue){
        this.keyValue = keyValue;
    }

    public Map<String, String> getKeyValue() {
        return keyValue;
    }
}
