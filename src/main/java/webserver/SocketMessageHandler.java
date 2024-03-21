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
import java.util.Arrays;

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
            log.debug("Send : " + response.getStartLine().toString() + " for " + request.getStartLine().toString());
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
        MessageHeader.HeaderBuilder builder = MessageHeader.builder();
        String reqLine;
        while ((reqLine = br.readLine()) != null && !reqLine.isEmpty()) {
            String[] headerField = reqLine.split(": ");
            builder.field(headerField[0], headerField[1]);
        }
        request.header(builder.build());

        // body
        char[] body;
        if (request.getHeaderValue("Content-Length") != null) {
            body = new char[Integer.parseInt(request.getHeaderValue("Content-Length"))];
            br.read(body);

            FileType fileType = FileType.of(request);
            request.body(new MessageBody(new String(body), fileType));
        }

        return request;
    }
}