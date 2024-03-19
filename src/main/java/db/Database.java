package db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.User;

public class Database {
    private static final Map<String, User> users = new ConcurrentHashMap<>();

    static {
        users.put("sangchu", new User("sangchu", "123", "상추", "sangchu@gmail.com"));
    } // 테스트를 위한 추가

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public static void clear() {
        users.clear();
    }
}
