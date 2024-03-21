package webserver.HttpHandler;

import db.SessionStore;
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
            if(SessionStore.getSession(request.getHeaderValue("Cookie").split("sid=")[1]) != null){
                return true;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException noCookieSid) {
            return false;
        }
        return false;
    }
}
