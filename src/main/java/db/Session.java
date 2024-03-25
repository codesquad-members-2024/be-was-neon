package db;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Session {
    private static final Map<String, String> session = new HashMap<>(); // key: 세션id, value: user id
    public static final int SESSION_ID_LENGTH = 6;

    public static String storeSession(String userId){ // 입력받은 userId를 임의의 sessionId와 함께 저장
        String sessionId = makeSessionId();
        session.put(sessionId, userId);
        return sessionId;
    }

    public static void deleteSession(String sessionId){
        session.remove(sessionId);
    }

    private static String makeSessionId(){
        Random random = new Random();

        return IntStream.range(0, SESSION_ID_LENGTH)
                .mapToObj(i -> random.nextInt(10)) // 0~9 숫자를 무작위로 선택
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public static boolean isSessionExist(String sessionId){
        return session.containsKey(sessionId);
    }

    public static String getSessionUserId(String sessionId){
        return session.get(sessionId);
    }
}
