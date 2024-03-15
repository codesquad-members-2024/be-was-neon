package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request.Request;
import webserver.Response.Response;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = readRequestMessage(in);

            DataOutputStream dos = new DataOutputStream(out);
            Response response = new Response(request);

            sendResponse(dos, response);
            log.debug(new String(response.getHeader()));
            log.info(request.getLog() + " Complete");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) throws IOException {
        byte[] header = response.getHeader();
        byte[] body;

        dos.write(header, 0, header.length);
        if ((body = response.getBody()).length != 0) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    private static Request readRequestMessage(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // start -line
        String startLine = br.readLine();
        if (startLine == null) throw new IOException("null request message");

        //header
        Map<String, String> headerFields = new HashMap<>();
        String reqLine = "";
        while ((reqLine = br.readLine()) != null && !reqLine.isEmpty()) {
            String[] headerField = reqLine.split(": ");
            headerFields.put(headerField[0], headerField[1]);
        }

        // body
        char[] body = {};
        if (headerFields.containsKey("Content-Length")) {
            body = new char[Integer.parseInt(headerFields.get("Content-Length"))];
            br.read(body);
        }

        return new Request(startLine, headerFields, new String(body));
    }
}