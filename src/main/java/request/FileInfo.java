package request;

import java.io.File;

public class FileInfo {
    private static final String BASIC_STATIC_FILE_PATH = "src/main/resources/static";
    private static final String INDEX_FILE_NAME = "/index.html";
    public static final String REGISTER_ACTION = "/user/create";
    private static final String DOT = "\\.";

    public static String makeCompletePath(String url){ // /registration
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

    public static String getFileType(String getCompletePath){
        String[] splitPath = getCompletePath.split(DOT);
        return splitPath[1]; // .으로 split 했을 때 idx:1이 타입
    }

    public static boolean checkValidFile(File file){
        return (file.exists() && !file.isDirectory());
    }
}
