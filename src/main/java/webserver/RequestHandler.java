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

            String line = br.readLine();
            httpRequest.storeStartLineData(line); // start line 저장
            logger.debug("request line : {}", httpRequest.getStartLine());

            line = br.readLine();
            while(!line.isEmpty()){ // 나머지 header 출력
                httpRequest.storeHeadersData(line); // header 저장
                logger.debug("request header : {}", line);
                line = br.readLine();
            }

            if(httpRequest.isPost() && httpRequest.isUserCreate()){ // Post, /user/create 라면
                char[] buffer = new char[httpRequest.getContentLength()];
                br.read(buffer, 0, buffer.length); // context length 만큼 읽기

                httpRequest.storeBodyData(new String(buffer)); // body 저장
                httpRequest.storeDatabase(httpRequest.createUser()); // user 생성 후 db에 저장
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
        String completePath = FileInfo.getCompletePath(httpRequest.getUrl());
        String contentType = ContentType.getContentType(FileInfo.getFileType(completePath));

        File file = new File(completePath);
        if(FileInfo.checkValidFile(file)){ // 파일이 존재하는지 확인
            FileInputStream fis = new FileInputStream(file);
            byte[] fileContent = fis.readAllBytes();
            fis.close();

            if(httpRequest.isPost() && httpRequest.isUserCreate()){
                httpResponseHeader.response302("/index.html", contentType, fileContent.length); // 처음 페이지로 redirect
                httpResponseBody.setBody(fileContent);
            }else{
                httpResponseHeader.response200(contentType, fileContent.length);
                httpResponseBody.setBody(fileContent);
            }

        }else{
            byte[] fileContent = "<h1>404 Not Found</h1>".getBytes();
            httpResponseHeader.response404(contentType, fileContent.length);
            httpResponseBody.setBody(fileContent);
        }
        dos.flush();
    }

}