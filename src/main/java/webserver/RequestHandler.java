package webserver;

import java.io.*;
import java.net.Socket;

import manager.DefaultManager;
import manager.LoginManager;
import manager.LogoutManager;
import manager.RegisterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import request.RequestParser;
import response.HttpResponse;
import response.ResponseManager;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = RequestParser.readRequestMessage(in);

            HttpResponse httpResponse;

            if(httpRequest.getStartLineInfo("url").equals("/registration")){
                RegisterManager registerManager = new RegisterManager(httpRequest);
                httpResponse = registerManager.responseMaker();
            }else if(httpRequest.getStartLineInfo("url").equals("/login")){
                LoginManager loginManager = new LoginManager(httpRequest);
                httpResponse = loginManager.responseMaker();
            }else if(httpRequest.getStartLineInfo("url").equals("/logout")){
                LogoutManager logoutManager = new LogoutManager(httpRequest);
                httpResponse = logoutManager.responseMaker();
            }else{
                DefaultManager defaultManager = new DefaultManager(httpRequest);
                httpResponse = defaultManager.responseMaker();
            }

            DataOutputStream dos = new DataOutputStream(out);
            ResponseManager responseManager = new ResponseManager(httpResponse);

            responseManager.writeResponse(dos);
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}