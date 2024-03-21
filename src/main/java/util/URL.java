package util;

import java.io.File;

public class URL {
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String RESOURCES_PATH = File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "static";

    public static File getFile(String uri) {
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return new File(ROOT_PATH + RESOURCES_PATH + uri);
    }


}
