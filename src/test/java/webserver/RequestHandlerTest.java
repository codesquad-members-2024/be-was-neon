package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;


import static org.junit.jupiter.api.Assertions.assertTrue;
class RequestHandlerTest {


    @DisplayName("index.html 리소스를 요청할 때 네트워크로 응답이 반환된다.")
    @Test
    void test3() throws IOException {
        Socket socketMock = mock(Socket.class);

        InputStream inputStreamMock = new ByteArrayInputStream("GET /index.html HTTP/1.1\n\n".getBytes());
        OutputStream outputStreamMock = new ByteArrayOutputStream();

        when(socketMock.getInputStream()).thenReturn(inputStreamMock);
        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);


        RequestHandler requestHandler = new RequestHandler(socketMock, inputStreamMock, outputStreamMock);
        requestHandler.run();

        String output = outputStreamMock.toString();
        assertTrue(output.contains("HTTP/1.1 200 OK"));//응답확인
    }

    @DisplayName("회원가입 페이지를 요청할 때 요청할 때 네트워크로 응답이 반환된다.")
    @Test
    void test4() throws IOException {
        Socket socketMock = mock(Socket.class);

        InputStream inputStreamMock = new ByteArrayInputStream("GET /register.html HTTP/1.1\n\n".getBytes());
        OutputStream outputStreamMock = new ByteArrayOutputStream();

        when(socketMock.getInputStream()).thenReturn(inputStreamMock);
        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        RequestHandler requestHandler = new RequestHandler(socketMock, inputStreamMock, outputStreamMock);
        requestHandler.run();

        String output = outputStreamMock.toString();
        assertTrue(output.contains("HTTP/1.1 200 OK"));//응답확인
    }

}