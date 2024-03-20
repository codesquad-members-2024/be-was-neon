package response;


import java.io.*;

public class HttpFileHandler {
    public static byte[] htmlToByte(String fileName) throws IOException {
        // 파일에서 바이트 단위로 읽기 위한 스트림을 제공합니다.
        FileInputStream fis = new FileInputStream(new File(fileName));
        //FileInputStream 의 성능을 개선하기 위한 보조 스트림으로, 버퍼링된 입력 스트림입니다.
        BufferedInputStream bis = new BufferedInputStream(fis);
        //바이트 배열에 데이터를 쓰기 위한 스트림입니다.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        //read()는 파일의 끝에 도달하면 -1을 반환합니다.
        while ((bytesRead = bis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        // 파일 데이터를 fileBytes 배열로 읽어옵니다.
        return baos.toByteArray();
    }
}
