package webserver;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import db.Database;
import model.User;

public class HttpRequest {
    public static final String METHOD_GET = "GET";
    private static final String DOT = "\\.";
    private static final String SPACE = " ";
    private static final String COLON_SPACE = ": ";


    private String startLine;
    private String body;

    private HashMap<String, String> startLineData;
    private HashMap<String, String> headersData;
    private HashMap<String, String> bodyData;

    HttpRequest() {
        this.startLineData = new HashMap<String, String>();
        this.headersData = new HashMap<String, String>();
        this.bodyData = new HashMap<String, String>();
    }

    public void storeStartLineData(String startLine){
        this.startLine = startLine;
        parseStartLine(startLine);
    }

    public void storeHeadersData(String headerLine){ // headers 한줄마다 HashMap에 저장
        String[] splitHeaderLine = headerLine.split(COLON_SPACE);
        headersData.put(splitHeaderLine[0], splitHeaderLine[1]);
    }

    private void parseStartLine(String startLine){
        String[] splitStartLine = startLine.split(SPACE);
        startLineData.put("method", splitStartLine[0]);
        startLineData.put("url", splitStartLine[1]);
        startLineData.put("version", splitStartLine[2]);
    }


    // ------------------------- request body 데이터 파싱&저장 -------------------------
    public void storeBody(String body){
        this.body = body;
        parseBodyData();
    }

     // 회원가입 정보 파싱
    public void parseBodyData() {
        for(String token : body.split("&")){
            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
            try {
                bodyData.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // 회원가입 데이터 DB에 추가
    public void storeDatabase(){
        Database.addUser(new User(bodyData.get("userId"), bodyData.get("password"), bodyData.get("name"), bodyData.get("email")));
    }


    // -------------------------  getter -------------------------
    public String getStartLine(){
        return startLine;
    }

    public String getMethod(){
        return startLineData.get("method");
    }

    public String getUrl(){
        return startLineData.get("url");
    }

    public String getVersion(){
        return startLineData.get("version");
    }

    public int getContentLength(){
        return Integer.parseInt(headersData.get("Content-Length"));
    }

    public boolean isPost(){
        return startLineData.get("method").equals("POST");
    }

    public boolean isUserCreate(){
        return startLineData.get("url").equals("/user/create");
    }
}
