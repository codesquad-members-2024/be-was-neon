package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class FileReader {

    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);

    public byte[] responseBody(File file) {
        byte[] body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
            return body;
        } catch (IOException e) {
            return null;
        }
    }
}
