package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
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

            HttpRequest httpRequest = new HttpRequest();

            String requestLine = br.readLine();
            httpRequest.storeStartLineData(requestLine);
            logger.debug("request method : {}", httpRequest.getStartLine());

//            if(httpRequest.checkRegisterDataEnter()){ // 회원가입에서 보낸 정보라면
//                storeUser(httpRequest); // user 생성 후 저장
//            }

            requestLine = br.readLine();
            while(!requestLine.isEmpty()){ // 나머지 header 출력
                httpRequest.storeHeadersData(requestLine);
                logger.debug("request header : {}", requestLine);
                requestLine = br.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);
            sendResponse(dos, httpRequest);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, HttpRequest httpRequest) throws IOException {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(dos);
        HttpResponseBody httpResponseBody = new HttpResponseBody(dos);

        File file = new File(httpRequest.getCompletePath());
        if(checkValidFile(file)){ // 파일이 존재하면 해당 파일을 읽어 응답.
            FileInputStream fis = new FileInputStream(file);
            byte[] fileContent = fis.readAllBytes();
            fis.close();

            if(httpRequest.checkRegisterDataEnter()){
                httpResponseHeader.setStartLine("302", "FOUND");
                httpResponseHeader.setLocation("/index.html"); // redirect 경로 지정
            }else{
                httpResponseHeader.setStartLine("200", "OK");
            }
            httpResponseHeader.setContentType(ContentType.getContentType(httpRequest.getFileType()));
            httpResponseHeader.setContentLength(fileContent.length);
            httpResponseBody.setBody(fileContent);
        }else{
            byte[] fileContent = "<h1>404 Not Found</h1>".getBytes();

            httpResponseHeader.setStartLine("404", "Not Found");
            httpResponseHeader.setContentType(ContentType.getContentType(httpRequest.getFileType()));
            httpResponseHeader.setContentLength(fileContent.length);
            httpResponseBody.setBody(fileContent);
        }
        dos.flush();
    }

    private void storeUser(HttpRequest httpRequest){
        httpRequest.parseRegisterData();
        httpRequest.storeDatabase();
    }

    private boolean checkValidFile(File file){
        return (file.exists() && !file.isDirectory());
    }
}