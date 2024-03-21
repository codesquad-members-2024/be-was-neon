package utils;

import java.io.File;

public class PathUtils {

    private static final String STATIC_RESOURCES_PATH = "src/main/resources/static";
    private static final String STATIC_DEFAULT_PATH = "/index.html";
    private static final String EXTENSION_DELIMITER = ".";

    public static String getStaticDefaultPath() {
        return STATIC_DEFAULT_PATH;
    }

    public static String getStaticResourcePath(String absolutePath) {
        File file = new File(STATIC_RESOURCES_PATH + absolutePath);
        if (file.isDirectory()) {
            return file.getPath() + STATIC_DEFAULT_PATH;
        }
        return file.getPath();
    }

    public static String getExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(EXTENSION_DELIMITER)+1);
    }
}