package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Utils.HttpRequestParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String firstPath; // 첫 번째 요청된 경로(index.html)를 저장할 변수

    public RequestHandler(Socket connectionSocket) {

        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 요청 라인 읽기
            String requestLine = readRequestLine(br);
            // 요청 경로 파싱
            parseRequestPath(requestLine);
            // 헤더 읽기
            readHeaders(br);

            byte[] body = readFileContent();
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private String readRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        logger.debug("request line: {}", line);
        return line;
    }

    private void parseRequestPath(String requestLine) {
        HttpRequestParser httpRequestParser = new HttpRequestParser(requestLine);
        String path = httpRequestParser.makeCompletePath();

        firstPath = path; // 이전 조건문 제거
        logger.debug("Extracted path: {}", firstPath);
    }


    private void readHeaders(BufferedReader br) throws IOException {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            logger.debug("header: {}", line);
        }
    }

    private byte[] readFileContent() throws IOException {
        File file = new File( firstPath);
        if (!file.exists()) { // 파일이 존재하지 않는 경우
            logger.error("Requested file not found: {}", file.getAbsolutePath());
            // 파일이 존재하지 않을 경우 FileNotFoundException을 던집니다.
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        FileInputStream fileInputStream = null;
        byte[] data = new byte[(int) file.length()]; // 파일 크기만큼의 바이트 배열 생성

        try {
            fileInputStream = new FileInputStream(file);
            int bytesRead = fileInputStream.read(data); // 파일 내용을 바이트 배열에 읽어옴
            if (bytesRead != file.length()) {
                throw new IOException("File read error: Could not read the entire file");
            }
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close(); // 파일 입력 스트림을 안전하게 닫음
                } catch (IOException e) {
                    logger.error("Error closing file input stream.", e);
                }
            }
        }
        return data;
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