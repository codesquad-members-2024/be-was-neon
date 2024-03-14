package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpResponse;
import utils.IOUtils;
import utils.RegistrationResponse;

public class RequestHandler implements Runnable {
    private static final String RELATIVE_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            String fileName = IOUtils.getFileName(line);
            DataOutputStream dos = new DataOutputStream(out);

            IOUtils.showEveryHeaders(logger,br,line);
            // request 의 종류에 따라 다른 response 를 보내줄수있게 해주었습니다.
            if (fileName.endsWith(".html")) {
                HttpResponse.respondHtmlFile(dos, RELATIVE_PATH + fileName);
            }else if (fileName.startsWith("/create")){
                RegistrationResponse.respondRegistration(dos,fileName);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
