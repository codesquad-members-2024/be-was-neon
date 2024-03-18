package webserver;

import java.io.File;

public class GetPath {
    private static final String BASIC_STATIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";
    public static final String REGISTER_ACTION = "/user/create";

    public static String getCompletePath(String url){ // /registration
        StringBuilder completePath = new StringBuilder(BASIC_STATIC_FILE_PATH);
        if(!url.contains(REGISTER_ACTION)){
            completePath.append(url);
        }
        File file = new File(completePath.toString());
        if(file.isDirectory()){ // file이 아니라 폴더이면 "/index.html" 추가
            completePath.append(INDEX_FILE_NAME);
        }
        return completePath.toString();
    }

}
