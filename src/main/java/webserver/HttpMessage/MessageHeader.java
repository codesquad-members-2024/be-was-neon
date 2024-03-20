package webserver.HttpMessage;

import db.SessionStore;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
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

    public String addCookie(int length){
        String newCookie = makeCookie(length);

        ZonedDateTime dateTime = ZonedDateTime.now().plus(1 , ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");
        String formattedDateTime = dateTime.format(formatter);

        addHeaderField("Set-Cookie", "sid="+ newCookie + "; Path=/" + "; Expires=" + formattedDateTime);
        return newCookie;
    }

    private static String makeCookie(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        String newCookie;
        while (true) {
            sb.setLength(0);
            while (sb.length() < length) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                sb.append(randomChar);
            }
           newCookie = sb.toString();

            if(SessionStore.getSession(newCookie) == null) break;
        }
        return newCookie;
    }
}
