package manager;

import db.Session;
import request.FileInfo;
import request.HttpRequest;
import response.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class LogoutManager {
    HttpRequest httpRequest;
    HttpResponse httpResponse;

    public LogoutManager(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        httpResponse = new HttpResponse();
    }

    public HttpResponse responseMaker() throws IOException {
        removeSessionId(); // 세션 id 삭제

        String completePath = FileInfo.makeCompletePath("/index.html");

        File file = new File(completePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = fis.readAllBytes();
        fis.close();

        httpResponse.setStartLine("302", "FOUND");
        httpResponse.setLocation("/index.html");

        httpResponse.setBody(body);

        return httpResponse;
    }

    private void removeSessionId(){
        Session.deleteSession(parseSidValue());
        // 해당 session id가 없을 경우도 생각해야...
    }

    private String parseSidValue(){ // header의 cookie정보에서 sid 값을 추출
        HashMap<String, String> cookieData = new HashMap<String, String>();

        for(String token : httpRequest.getHeaderInfo("Cookie").split("[\\s;]+")){ // " "과 ";" 하나이상 반복된 패턴으로 split
            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
            cookieData.put(splitInfo[0], splitInfo[1]); // 해쉬 맵에 정보 저장
        }
        return cookieData.get("sid");
    }
}