package webserver.HttpMessage;

import webserver.HttpMessage.constants.WebServerConst;
import webserver.HttpMessage.constants.eums.FileType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static webserver.SocketMessageHandler.biReadLine;
import static webserver.WebServer.staticSourcePath;

public class MultiMessageBody {
    List<MessageBody> bodyList = new ArrayList<>();

    public MultiMessageBody(byte[] body, String boundary) throws IOException {
        BufferedInputStream bis;
        FileType fileType;


//        for (byte[] splitted : splitBytes(body, boundary)) {
            bis = new BufferedInputStream(new ByteArrayInputStream(body));
//            if(splitted.length == 0) continue;
//            if(new String(splitted).equals("--")) break;

            biReadLine(bis); // CRLF
            String description = biReadLine(bis); // Content-Description
            String contentType = biReadLine(bis); // Content-Type

            if (contentType.equals("")) {
                fileType = FileType.TXT;
            } else fileType = FileType.of(contentType.split(WebServerConst.HEADER_DELIM)[1]);
            biReadLine(bis); // CRLF

            // Content data
            byte[] content = new byte[597959];
            bis.read(content , 0 , 597959);
            bodyList.add(new MessageBody(content, fileType));

            if (fileType.equals(FileType.PNG)) {
                OutputStream outputStream = new FileOutputStream(staticSourcePath + "/img/post/output.png");
                outputStream.write(content);
                outputStream.close();
            }
//        }
    }

//    private List<byte[]> splitBytes(byte[] bytes, String delim) {
//        List<byte[]> splitted = new ArrayList<>();
//        String data = new String(bytes);
//
//        // 문자열을 구분자를 기준으로 나누기
//        int index;
//        int lastIndex = 0;
//        while ((index = data.indexOf(delim, lastIndex)) != -1) {
//            // 현재 구분자 위치까지의 문자열을 바이트 배열로 변환하여 리스트에 추가
//            byte[] part = data.substring(lastIndex, index).getBytes();
//            splitted.add(part);
//            // 다음 검색을 위해 lastIndex를 현재 위치 다음으로 설정
//            lastIndex = index + delim.length();
//        }
//
//        // 마지막 구분자 이후의 문자열을 바이트 배열로 변환하여 리스트에 추가
//        byte[] lastPart = data.substring(lastIndex).getBytes();
//        splitted.add(lastPart);
//
//        return splitted;
//    }
}
