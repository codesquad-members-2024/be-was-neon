package http.handler;

import static utils.Constant.BASE_PATH;

import db.UserDatabase;
import http.response.Status;
import model.User;
import utils.UserGenerator;

public class UserCreateHandler extends CommandHandler {
    @Override
    public void handleGetRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    @Override
    public void handlePostRequest() {
        UserGenerator userGenerator = new UserGenerator(requestManager);
        User user = userGenerator.createUser();
        UserDatabase.addUser(user);
        responseManager.setRedirectResponse(BASE_PATH);
    }
}
