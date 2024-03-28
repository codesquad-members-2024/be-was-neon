package http.handler;

import static utils.Constant.BASE_PATH;

import session.SessionManager;

public class UserLogoutHandler extends CommandHandler {
    @Override
    public void handlePostRequest() {
        SessionManager sessionManager = new SessionManager(requestManager);
        sessionManager.expireSession();
        responseManager.setRedirectResponse(BASE_PATH);
    }
}
