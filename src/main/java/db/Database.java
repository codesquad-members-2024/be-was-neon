package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class Database {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
