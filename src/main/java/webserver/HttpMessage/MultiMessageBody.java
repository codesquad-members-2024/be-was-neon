package webserver.HttpMessage;

import webserver.HttpMessage.constants.WebServerConst;
import webserver.HttpMessage.constants.eums.FileType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static webserver.HttpMessage.constants.WebServerConst.CRLF;
import static webserver.WebServer.staticSourcePath;

public class MultiMessageBody{
    List<MessageBody> bodyList = new ArrayList<>();

    public MultiMessageBody(String body , String boundary) throws IOException {
        String[] split = body.split("--" + boundary);
        BufferedReader br;

        for(String s : split){
            if(s.equals("--")) break;
            if(s.isEmpty()) continue;

            br = new BufferedReader(new StringReader(s));
            br.readLine(); // CRLF
            String description = br.readLine(); // Content-Description
            String contentType = br.readLine(); // Content-Type
            FileType fileType;
            if(contentType.equals("")){
                fileType = FileType.TXT;
            }
            else fileType = FileType.of(contentType.split(WebServerConst.HEADER_DELIM)[1]);
            br.readLine(); // CRLF

            StringBuilder sb = new StringBuilder();
            String nextLine;
            while ((nextLine = br.readLine()) != null){ // Content
                sb.append(nextLine).append(CRLF);
            }
            sb.delete(sb.length()-2 , sb.length()-1);

            bodyList.add(new MessageBody(sb.toString() , fileType));

            if(fileType.equals(FileType.PNG)){
                OutputStream outputStream = new FileOutputStream(staticSourcePath + "/img/post/output.png");
                outputStream.write( sb.toString().getBytes());
                outputStream.close();
                System.out.println("저장됨");
            }
        }

        System.out.println("complete");
    }
}
