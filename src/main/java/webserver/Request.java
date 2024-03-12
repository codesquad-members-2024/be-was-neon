package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private String method;
    private String reqDetail;
    private String url;
    private FileType fileType;
    private List<String> params;

    private Request() {
    }

    public static Request makeRequest(InputStream in) throws IOException {
        String[] requestHeader = parseRequest(in);
        Request request = new Request();
        request.method = requestHeader[0];
        request.url = requestHeader[1];


        if (createUserHeader.matcher(request.url).matches()) {
            request.reqDetail = "createUser";
            setCreateParams(request);
        }
        else if (getFileHeader.matcher(request.url).matches()) {
            request.reqDetail = "getFile";
            setFileReqUrl(request);
        }

        return request;
    }

    private static void setCreateParams(Request request) {
        Pattern param = Pattern.compile("=\\w+(%40\\w+\\.com)?");
        Matcher matcher = param.matcher(request.url);
        List<String> createParameters = new ArrayList<>();

        while (matcher.find()) {
            createParameters.add(matcher.group().substring(1)); // '=' 제거
        }
        request.params = createParameters;
    }

    private static void setFileReqUrl(Request request) {
        try {
            // valueOf(~.toUpperCase()) 로 파일타입 Enum 에서 타입 찾아 지정
            request.fileType = FileType.valueOf(request.url.split("\\.")[1].toUpperCase());
        } catch (ArrayIndexOutOfBoundsException justPath) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
            request.url = request.url + "/index.html";
            request.fileType = FileType.HTML;
        }
    }

    private static String[] parseRequest(InputStream in) throws IOException {
        String requestHeader;
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        requestHeader = br.readLine();
        return requestHeader.split(" ");
    }

    public String getReqDetail() {
        return reqDetail;
    }

    public String getUrl() {
        return url;
    }

    public FileType getFileType() {
        return fileType;
    }

    public List<String> getParams() {
        return params;
    }

    public String getLog(){
        StringJoiner sj = new StringJoiner(" ");
        sj.add(reqDetail);
        sj.add(url);

        return sj.toString();
    }

    private static final Pattern getFileHeader = Pattern.compile("((\\/?.)*)(\\.+\\w)?");
    private static final Pattern createUserHeader = Pattern.compile("\\/create\\?userId\\=+(\\w)+(&password=)+(\\w)+(&name=)(\\w)+(&email=)(.*%40\\w+\\.com)");
}
