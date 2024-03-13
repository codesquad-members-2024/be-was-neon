package webserver;

import java.io.*;
import java.net.Socket;
import Utils.FileUtils;
import Utils.HTTPRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String firstPath; // 첫 번째 요청된 경로(index.html)를 저장할 변수

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try {
            processRequest();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void processRequest() throws IOException {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            String requestLine = readRequestLine(br);
            parseRequest(requestLine);
            readHeaders(br);

            if (firstPath != null) {
                byte[] body = readFileContent();
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        }
    }

    private String readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        logger.debug("request line: {}", requestLine);
        return requestLine;
    }

    private void parseRequest(String requestLine) {
        HTTPRequestParser requestParser = new HTTPRequestParser();
        requestParser.parseRequestLine(requestLine);
        String path = requestParser.getPath();
        if ("/index.html".equals(path)) {
            firstPath = path;
            logger.debug("Extracted path: {}", firstPath);
        }
    }

    private void readHeaders(BufferedReader br) throws IOException {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            logger.debug("header: {}", line);
        }
    }

    private byte[] readFileContent() throws IOException {
        File file = new File("src/main/resources/static" + firstPath);
        FileUtils fileUtils = new FileUtils(file);
        return fileUtils.readFileToByteArray();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
