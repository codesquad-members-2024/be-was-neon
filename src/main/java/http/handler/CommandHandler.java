package http.handler;

import static utils.Constant.CONTENT_TYPE_DELIMITER;

import http.RequestManager;
import http.ResponseManager;
import http.request.FilePath;
import http.response.ContentType;
import http.response.Status;

public abstract class CommandHandler {
    protected RequestManager requestManager;
    protected ResponseManager responseManager;

    public void setManagers(RequestManager requestManager, ResponseManager responseManager) {
        this.requestManager = requestManager;
        this.responseManager = responseManager;
    }

    public void handleGetRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handlePostRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handlePutRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handleDeleteRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handleHeadRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handleConnectRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handleTraceRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    public void handlePatchRequest() {
        responseManager.setErrorResponse(Status.BAD_REQUEST);
    }

    protected ContentType getContentType(FilePath filePath) {
        String[] parts = filePath.getFilePathUrl().split(CONTENT_TYPE_DELIMITER);
        String type = parts[parts.length - 1];

        for (ContentType contentType : ContentType.values()) {
            if (contentType.name().equals(type)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException();
    }
}