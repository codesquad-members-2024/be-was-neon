package login;

import db.Database;
import java.util.Optional;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginManager {
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    public Optional<User> login(String id, String password) {
        Optional<User> optionalUser = Database.findUserById(id);

        if (optionalUser.isEmpty()) {
            logger.debug("[LOGIN MANAGER] Not Found Id = {}", id);
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        logger.debug("[LOGIN MANAGER] password error = {}", password);
        return Optional.empty();
    }
}
