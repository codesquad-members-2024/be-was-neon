package http.handler;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class CommandMatcher {
    public enum Command {
        USER_CREATE("/user/create", new UserCreateHandler()),
        USER_LOGIN("/user/login", new UserLoginHandler()),
        USER_LOGOUT("/user/logout", new UserLogoutHandler()),
        USER_LIST("/user/list", new UserListHandler());

        public final String filePath;
        public final CommandHandler commandHandler;

        Command(String filePath, CommandHandler commandHandler) {
            this.filePath = filePath;
            this.commandHandler = commandHandler;
        }
    }

    public static boolean isValidCommand(String filePath) {
        return Arrays.stream(Command.values()).anyMatch(command -> command.filePath.equals(filePath));
    }

    public static CommandHandler matchCommandHandler(String filePath) throws FileNotFoundException {
        return Arrays.stream(Command.values()).filter(command -> command.filePath.equals(filePath))
                .map(command -> command.commandHandler)
                .findFirst()
                .orElseThrow(FileNotFoundException::new);
    }
}
