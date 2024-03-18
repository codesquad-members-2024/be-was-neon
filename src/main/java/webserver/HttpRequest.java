package webserver;

import db.Database;
import model.User;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

public class HttpRequest {
//    private static final String BASIC_FILE_PATH = "src/main/resources/static";
//    private static final String INDEX_FILE_NAME = "/index.html";
//    public static final String REGISTER_ACTION = "/user/create";
    public static final String METHOD_GET = "GET";
    private static final String DOT = "\\.";
    private static final String SPACE = " ";
    private static final String EQUAL = "=";


    private String startLine;

    private HashMap<String, String> startLineData;
    private HashMap<String, String> headersData;

    HttpRequest() {
        this.startLineData = new HashMap<String, String>();
        this.headersData = new HashMap<String, String>();
    }

    public void storeStartLineData(String startLine){
        this.startLine = startLine;
        parseStartLine(startLine);
    }

    public void storeHeadersData(String headerLine){ // headers 한줄마다 HashMap에 저장
        String[] splitHeaderLine = headerLine.split(EQUAL);
        headersData.put(splitHeaderLine[0], splitHeaderLine[1]);
    }

    // 회원가입 정보 파싱
//    public void parseRegisterData() {
//        for(String token : extractUserData().split("[& ]")){
//            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
//            try {
//                registerUserData.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 회원가입 정보 파싱
//    private String extractUserData(){ // 사용자 입력 정보 부분만 반환
//        String[] splitStartLine = url.split("[?]");
//        return splitStartLine[1];
//    }

    // 회원가입 데이터 DB에 추가
//    public void storeDatabase(){
//        Database.addUser(new User(registerUserData.get("userId"), registerUserData.get("password"), registerUserData.get("name"), registerUserData.get("email")));
//    }

//    public boolean checkRegisterDataEnter(){ // 회원가입에서 보낸 GET인지 확인
//        return (method.equals(METHOD_GET) && url.startsWith(REGISTER_ACTION));
//    }


    private void parseStartLine(String startLine){
        String[] splitStartLine = startLine.split(SPACE);
        startLineData.put("method", splitStartLine[0]);
        startLineData.put("url", splitStartLine[1]);
        startLineData.put("version", splitStartLine[2]);
    }

//    public String getCompletePath(){ // /registraion
//        StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);
//        if(!url.contains(REGISTER_ACTION)){
//            completePath.append(url);
//        }
//        File file = new File(completePath.toString());
//        if(file.isDirectory()){ // file이 아니라 폴더이면 "/index.html" 추가
//            completePath.append(INDEX_FILE_NAME);
//        }
//        return completePath.toString();
//    }

//    public String getFileType(){
//        String[] splitPath = getCompletePath().split(DOT);
//        return splitPath[1]; // .으로 split 했을 때 idx:1이 타입
//    }

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

//    public String getUserId(){
//        return registerUserData.get("userId");
//    }
//
//    public String getPassword(){
//        return registerUserData.get("password");
//    }
//
//    public String getName(){
//        return registerUserData.get("name");
//    }
//
//    public String getEmail(){
//        return registerUserData.get("email");
//    }
}
