package webserver;

import java.io.*;
import java.net.Socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestHandler implements Runnable {
    public static final String REGISTER_ACTION = "/user/create";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);

            // String requestLine = br.readLine();
            logger.debug("request method : {}", httpRequest.getStartLine());

            if(checkRegisterInput(httpRequest.getStartLine())){
                RegisterRequestHandler registerRH = new RegisterRequestHandler(httpRequest.getStartLine());
            }

            // 요청 받은 URL을 파싱하여 파일 경로를 결정한다.
            String filePath = httpRequest.getCompletePath();

//            requestLine = br.readLine();
//            while(!requestLine.isEmpty()){ // 나머지 header 출력
//                logger.debug("request header : {}", requestLine);
//                requestLine = br.readLine();
//            }

            // 파일이 존재하면 해당 파일을 읽어 응답.
            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] fileContent = new byte[(int) file.length()];
                fis.read(fileContent);
                fis.close();

                response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);
            } else {
                // 파일이 존재하지 않으면 404 응답.
                byte[] notFoundContent = "<h1>404 Not Found</h1>".getBytes();
                response404Header(dos, notFoundContent.length);
                responseBody(dos, notFoundContent);
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

    private void response302Header(DataOutputStream dos){ // 회원가입 정보 받고 redirect에 사용
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND\r\n");
            dos.writeBytes("Location: " + StringUtils.INDEX_FILE_NAME + "\r\n"); // redirect 경로 지정
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
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

    private boolean checkRegisterInput(String startLine){ // 회원가입에서 보낸 GET인지 확인
        return startLine.contains(REGISTER_ACTION);
    }
}