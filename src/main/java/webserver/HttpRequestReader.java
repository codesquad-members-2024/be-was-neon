package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequestReader {
    private final BufferedReader reader;

    public HttpRequestReader(Socket connection) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

}
