package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

import model.User;
import db.Database;

public class RequestManager {
    private static final Logger logger = LoggerFactory.getLogger(RequestManager.class);
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private InputStream in;
    HttpRequest httpRequest;

    public RequestManager(InputStream in){
        this.in = in;
        this.httpRequest = new HttpRequest();
    }

    public void readRequestMessage() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        readStartLine(br);
        readHeaders(br);
        if(isMethodPost() && isUrlUserCreate() && httpRequest.isContentLengthExist()){
            readBody(br);
            storeDatabase(createUser()); // user 생성한 뒤 db에 저장
        }
    }

    private void readStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        httpRequest.storeStartLineData(line); // start line 저장
        logger.debug("request line : {}", line);
    }

    private void readHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while(!line.isEmpty()){ // 나머지 headers 순회
            httpRequest.storeHeadersData(line); // header 저장
            logger.debug("request header : {}", line);
            line = br.readLine();
        }
    }

    private void readBody(BufferedReader br) throws IOException {
        char[] buffer = new char[httpRequest.getContentLength()];
        br.read(buffer); // context length 만큼 읽기
        httpRequest.storeBodyData(new String(buffer)); // body 저장
    }

    // ------------------------- User 생성 및 저장 -------------------------
    public User createUser(){
        return new User(
                httpRequest.getBodyInfo("userId"),
                httpRequest.getBodyInfo("password"),
                httpRequest.getBodyInfo("name"),
                httpRequest.getBodyInfo("email"));
    }

    // 회원가입 데이터 DB에 추가
    public void storeDatabase(User user){
        Database.addUser(user);
    }

    // ------------------------- getter -------------------------
    public boolean isMethodGet(){
        return httpRequest.getStartLineInfo("method").equals(METHOD_GET);
    }

    public boolean isMethodPost(){
        return httpRequest.getStartLineInfo("method").equals(METHOD_POST);
    }

    public String getCompletePath(){
        return FileInfo.makeCompletePath(httpRequest.getStartLineInfo("url"));
    }

    public boolean isUrlUserCreate(){
        return httpRequest.getStartLineInfo("url").equals("/user/create");
    }
}