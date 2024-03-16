package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMessage.MessageBody;
import webserver.HttpMessage.MessageHeader;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.Response;
import webserver.Mapping.MappingMatcher;
import webserver.eums.FileType;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketMessageHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SocketMessageHandler.class);
    private final Socket connection;

    public SocketMessageHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);

            Request request = getRequest(in);
            log.debug(request.toString());

            MappingMatcher matcher = new MappingMatcher(request);
            Response response = matcher.getResponse();

            dos.writeBytes(response.toString());
            if(response.getBody() != null) dos.write(response.getBody());
            log.info("Send : " + response.getStartLine().toString() + " for " + request.getStartLine().toString());
            log.debug("Send \n" + response);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Request getRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // start -line
        String startLine = br.readLine();
        if (startLine == null) throw new IOException("null request message");
        Request request = new Request(startLine);

        //header
        Map<String, String> headerFields = new HashMap<>();
        String reqLine;
        while ((reqLine = br.readLine()) != null && !reqLine.isEmpty()) {
            String[] headerField = reqLine.split(": ");
            headerFields.put(headerField[0], headerField[1]);
        }
        request.header(new MessageHeader(headerFields));

        // body
        char[] body;
        if (headerFields.containsKey("Content-Length")) {
            body = new char[Integer.parseInt(headerFields.get("Content-Length"))];
            br.read(body);
            request.body(new MessageBody(new String(body), FileType.valueOf(headerFields.get("Content-Type"))));
        }

        return request;
    }
}