package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        if(contains(user)) throw new IllegalArgumentException("이미 존재하는 회원 id");
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) throws NullPointerException{
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    private static boolean contains(User user){
        return users.containsKey(user.getUserId());
    }
}
