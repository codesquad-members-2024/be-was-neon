package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.parseRequestLines(in);

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(httpRequest);
            responseHandler.sendResponseDependOnRequest(dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
//    private HttpRequest receiveRequest(InputStream in) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//        String line = URLDecoder.decode(br.readLine(), "UTF-8");
//        String requestLine = line;
//
//        logger.debug("request line : {}", line);
//
//        Map<String, String> headers = new HashMap<>();
//
//        while (true) {
//            line = URLDecoder.decode(br.readLine(), "UTF-8");
//
//            if ((line = br.readLine()) != null && !line.isEmpty()) {
//                break;
//            }
//
//            Map<String, String> header = parseHeader(line);
//            headers.put(pair.getKey(), pair.getValue());
//
//            logger.debug("header : {}", line);
//        }
//
//        String requestMessageBody = URLDecoder.decode(IOUtils.readData(br, getContentLength(headers)), StandardCharsets.UTF_8);
//
//        return new HttpRequest(requestLine, headers, requestMessageBody);
//    }
}
