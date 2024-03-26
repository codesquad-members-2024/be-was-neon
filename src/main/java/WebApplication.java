import application.handler.LoginHandler;
import application.handler.UserHandler;
import webserver.HttpHandler.Handler;
import webserver.WebServer;

import java.util.List;

public class WebApplication {
    public static void main(String[] args) throws Exception {
        List<Handler> codeStargramHandlers = List.of(
                new LoginHandler(),
                new UserHandler()
        );

        WebServer webApplicationServer = new WebServer(codeStargramHandlers);

        webApplicationServer.startServer(args);
    }
}
