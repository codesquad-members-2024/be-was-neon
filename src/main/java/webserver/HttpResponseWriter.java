package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpResponseWriter extends OutputStream {
    private final DataOutputStream writer;

    public HttpResponseWriter(Socket connection) throws IOException {
        OutputStream output = connection.getOutputStream();
        this.writer = new DataOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException {
        writer.write(b);
    }

    @Override
    public void write(byte[] data) throws IOException {
        writer.write(data);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }
}
