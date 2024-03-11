package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
//        log.debug("New Client Connect! Connected IP : {}, Port : {}",
//        connection.getInetAddress(), connection.getPort());

        String line = null;
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in)); // Input Req
            DataOutputStream dos = new DataOutputStream(out);

            line = br.readLine();
            String[] request = line.split(" ");
            // 리퀘스트를 파싱해 타입, 요청한 파일 경로 얻음
            String requestType = request[0];
            String url = request[1];
            String fileType = request[1].split("\\.")[1]; // 파일 확장자

            log.info(requestType + " : " + url);

            try {
                File file = new File("src/main/resources/static" + url);

                if (fileType.equals("png")) {
                    response200Header(dos ,10000 , "image/png");
                    responseBody(dos, responseBodyImg(file, fileType));
                }
                else if(fileType.equals("css")){
                    responseBodyFile(dos , file , "text/css");
                }
                else {
                    responseBodyFile(dos, file, "text/html;charset=utf-8");
                }

            } catch (NullPointerException noFile) {
                response404(dos);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException a) {
            log.error(a.getMessage() + line);
        }
    }

    private byte[] responseBodyImg(File file, String extension) throws IOException {
        byte[] imageInByte;

        BufferedImage originalImage = ImageIO.read(file);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        ImageIO.write(originalImage, extension, byteArrayOS);
        byteArrayOS.flush();

        imageInByte = byteArrayOS.toByteArray();
        byteArrayOS.close();

        return imageInByte;
    }

    private void responseBodyFile(DataOutputStream dos, File file , String fileType) throws IOException {
        byte[] body = Files.readAllBytes(file.toPath());
        response200Header(dos, body.length, fileType);
        responseBody(dos, body);
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent , String contentType) {
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