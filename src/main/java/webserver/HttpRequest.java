package webserver;

import db.Database;
import model.User;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

public class HttpRequest {
    private static final String BASIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";
    public static final String REGISTER_ACTION = "/user/create";

    private String startLine;
    private String method;
    private String url;
    private String version;

    private HashMap<String, String> registerUserData;

    HttpRequest(String startLine) throws IOException {
        this.startLine = startLine;
        this.registerUserData = new HashMap<String, String>();
        parseStartLine(startLine);
    }

    // 회원가입 정보 파싱
    public void parseRegisterData() {
        for(String token : extractUserData().split("[& ]")){
            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
            try {
                registerUserData.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // 회원가입 정보 파싱
    private String extractUserData(){ // 사용자 입력 정보 부분만 반환
        String[] splitStartLine = url.split("[?]");
        return splitStartLine[1];
    }

    // 회원가입 데이터 DB에 추가
    public void storeDatabase(){
        Database.addUser(new User(registerUserData.get("userId"), registerUserData.get("password"), registerUserData.get("name"), registerUserData.get("email")));
    }

    public boolean checkRegisterDataEnter(){ // 회원가입에서 보낸 GET인지 확인
        return (method.equals("GET") && url.startsWith(REGISTER_ACTION));
    }


    private void parseStartLine(String startLine){
        String[] splitStartLine = startLine.split(" ");
        this.method = splitStartLine[0];
        this.url = splitStartLine[1];
        this.version = splitStartLine[2];
    }

    public String getCompletePath(){
        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);
        if(!url.contains(REGISTER_ACTION)){
            completePath.append(url);
        }
        File file = new File(completePath.toString());
        if(file.isDirectory()){ // file이 아니라 폴더이면 "/index.html" 추가
            completePath.append(INDEX_FILE_NAME);
        }
        return completePath.toString();
    }

    public String getStartLine(){
        return startLine;
    }

    public String getMethod(){
        return method;
    }

    public String getUrl(){
        return url;
    }

    public String getVersion(){
        return version;
    }
}
