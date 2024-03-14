package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String requestMessage = readRequestMessage(in);
            Request request = makeRequest(requestMessage);

            DataOutputStream dos = new DataOutputStream(out);
            Response response = new Response(request);

            sendResponse(dos, response);
            log.info(request.getLog() + " Complete");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos , Response response) throws IOException {
        byte[] header = response.getHeader();
        byte[] body = response.getBody();

        dos.write(header, 0, header.length);
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private static Request makeRequest(String requestMessage) {
        String[] requestHeaderFirst = requestMessage.split("\r\n")[0].split(" ");
        Request request = new Request(requestHeaderFirst[0], requestHeaderFirst[1]);
        log.info("Request : " + request.getLog());
        return request;
    }

    private static String readRequestMessage(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String reqLine = br.readLine();
        if (reqLine == null) {
            throw new IOException("null req");
        }
        sb.append(reqLine);

        log.info("read " + sb);
        return sb.toString();
    }
}