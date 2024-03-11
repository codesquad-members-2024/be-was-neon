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
        String line;
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            line = br.readLine();
            String[] request = line.split(" ");
            // 리퀘스트를 파싱해 타입, 요청한 파일 경로 , 파일 확장자 얻음
            String requestType = request[0];
            String url = request[1];
            String fileType = request[1].split("\\.")[1];
            log.info(requestType + " : " + url);

            File file = new File("src/main/resources/static" + url);
            try {
                byte[] body = responseBodyFile(file);
                response200Header(dos, body.length, FileType.valueOf(fileType.toUpperCase()).getContentType());
                responseBody(dos, body);
            }catch (IOException noSuchFile){ // 해당 경로의 파일이 없을때
                response404(dos);
                log.error(noSuchFile.getMessage());
            }

        } catch (IOException |  ArrayIndexOutOfBoundsException e) { // 리퀘스트 읽을 때 발생 예외
            log.error(e.getMessage());
        }
    }

    private byte[] responseBodyFile(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        return bytes;
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response404(OutputStream out) throws IOException {
        String errorMessage = "404 Not Found";
        out.write(("HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + errorMessage.length() + "\r\n" +
                "\r\n" +
                errorMessage).getBytes());
    }
}