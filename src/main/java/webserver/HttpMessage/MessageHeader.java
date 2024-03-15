package webserver.HttpMessage;

import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

public class MessageHeader {
    Map<String , String> headerFields;
    public MessageHeader(Map<String ,String> headerFields){
        this.headerFields = headerFields;
    }

    public Map<String, String> getHeaderFields() {
        return Collections.unmodifiableMap(headerFields);
    }

    public void addHeaderField(String key , String value){
        headerFields.put(key, value);
    }

    public String toString(){
        StringJoiner sj = new StringJoiner("\r\n");
        headerFields.keySet().forEach(key -> sj.add(key + ": " + headerFields.get(key)));
        return sj + "\r\n";
    }
}
