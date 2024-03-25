package webserver.HttpHandler;

import application.db.SessionStore;
import application.model.User;
import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.MessageHeader;
import webserver.HttpMessage.Request;


public interface Handler {

    default MessageHeader writeContentResponseHeader(MessageBody responseBody) {
        return MessageHeader.builder()
                .field("Content-Type", responseBody.getContentType().getMimeType())
                .field("Content-Length", responseBody.getContentLength() + "")
                .build();
    }

    default boolean verifySession(Request request) {
        try {
            if(getCookie(request) != null){
                return true;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException noCookieSid) {
            return false;
        }
        return false;
    }

    default User getCookie(Request request) {
        return SessionStore.getSession(request.getHeaderValue("Cookie").split("sid=")[1]);
    }
}
