package webserver;

import db.Database;
import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class RegisterRequestHandler {
    HashMap<String, String> registerInfo = new HashMap<String, String>(); //ex) <userID, daniel>

    RegisterRequestHandler(String startLine) {
        parseRegisterInfo(removeUseless(startLine));
        storeDatabase();
    }

    private void parseRegisterInfo(String registerLine) {
        for(String token : registerLine.split("[& ]")){
            String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
            try {
                registerInfo.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private String removeUseless(String startLine){ // 사용자 입력 정보 부분만 반환
        String[] splitStartLine = startLine.split("[? ]");
        return splitStartLine[2];
    }

    private void storeDatabase(){
        Database.addUser(new User(registerInfo.get("userId"), registerInfo.get("password"), registerInfo.get("name"), registerInfo.get("email")));
    }
}
