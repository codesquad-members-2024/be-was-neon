package webserver;

import java.io.File;
import java.util.Map;

public class TypeMapper {
    private final static Map<String, String> map = Map.ofEntries(
            Map.entry("html", "text/html"),
            Map.entry("css", "text/css"),
            Map.entry("svg", "image/svg+xml"),
            Map.entry("ico", "image/x-icon")
    );

    public static String getContentType(File file) {
        String extension = getExtension(file);
        if (map.containsKey(extension)) {
            return map.get(extension);
        }
        throw new IllegalArgumentException("존재하지 않는 타입입니다.");
    }

    private static String getExtension(File file) {
        String fileName = file.getName();
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }
}
