package webserver.HttpHandler;

import db.SessionStore;
import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.MessageHeader;
import webserver.HttpMessage.Request;

import java.util.HashMap;

public interface Handler {

    default MessageHeader writeContentResponseHeader(MessageBody responseBody) {
        MessageHeader responseHeader = new MessageHeader(new HashMap<>());
        responseHeader.addHeaderField("Content-Type", responseBody.getContentType().getMimeType());
        responseHeader.addHeaderField("Content-Length", responseBody.getContentLength() + "");

        return responseHeader;
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
