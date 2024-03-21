package util;

import db.Database;
import http.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class URL {

    public static File getFile(String uri) {
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return new File("./src/main/resources/static" + uri);
    }

}
