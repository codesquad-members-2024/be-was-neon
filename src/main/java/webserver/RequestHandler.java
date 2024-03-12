package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final String RELATIVE_PATH = "./src/main/resources/static/";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            String fileName = getFileName(line);
            DataOutputStream dos = new DataOutputStream(out);
            logger.debug(line);
            while (line != null && !line.isEmpty()) {
                // Request header 의 로그를 콘솔에 출력해 줍니다.
                line = br.readLine();
                logger.debug(line);
            }
            if (fileName.endsWith(".html")) {
                // html 파일을 읽어와 Bytes 로 변환한 후에 response 를 내보낸다.
                respondHtmlFile(dos, RELATIVE_PATH + fileName);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private String getFileName(String firstLine) throws IOException {
        // /index.html HTTP/1.1 string 형태로 받아와서 파일 이름을 사용할수있게 split 을 이용하여 index.html 형태로 파싱해줍니다.
        String[] getFile = firstLine.split(" ");
        return getFile[1];
    }
    private void respondHtmlFile (DataOutputStream dos, String fileName){
        try {
            // 파일에서 바이트 단위로 읽기 위한 스트림을 제공합니다.
            FileInputStream fis = new FileInputStream(new File(fileName));
            //FileInputStream 의 성능을 개선하기 위한 보조 스트림으로, 버퍼링된 입력 스트림입니다.
            BufferedInputStream bis = new BufferedInputStream(fis);
            //바이트 배열에 데이터를 쓰기 위한 스트림입니다.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            //read()는 파일의 끝에 도달하면 -1을 반환합니다.
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            // 파일 데이터를 fileBytes 배열로 읽어옵니다.
            byte[] fileBytes = baos.toByteArray();
            // response 를 http 로 보냅니다.
            response200Header(dos, fileBytes.length);
            responseBody(dos, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
