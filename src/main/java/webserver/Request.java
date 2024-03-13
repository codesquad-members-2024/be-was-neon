package webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private final String method;
    private String url;
    private String reqDetail;
    private FileType fileType;
    private List<String> params;

    public Request(String method , String url) {
        this.method = method;
        this.url = url;
        setRequestDetail();
    }

    private void setRequestDetail(){
        if (createUserHeader.matcher(url).matches()) {
            reqDetail = "createUser";
            setParams();
        }
        else if (getFileHeader.matcher(url).matches()) {
            reqDetail = "getFile";
            setFileReqUrl();
        }
    }

    private void setParams() {
        Pattern param = Pattern.compile("=\\w+(%40\\w+\\.com)?");
        Matcher matcher = param.matcher(url);
        List<String> createParameters = new ArrayList<>();

        while (matcher.find()) {
            createParameters.add(matcher.group().substring(1)); // '=' 제거
        }
        params = createParameters;
    }

    private void setFileReqUrl() {
        try {
            // valueOf(~.toUpperCase()) 로 파일타입 Enum 에서 타입 찾아 지정
            String[] type = url.split("\\.");
            fileType = FileType.valueOf(type[type.length-1].toUpperCase());
        } catch (IllegalArgumentException justPath) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
            url = url + "/index.html";
            fileType = FileType.HTML;
        }
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
        sj.add(reqDetail.toUpperCase());
        sj.add(" : ");
        sj.add(url);

        return sj.toString();
    }

    private static final Pattern getFileHeader = Pattern.compile("((\\/?.)*)(\\.+\\w)?");
    private static final Pattern createUserHeader = Pattern.compile("\\/create\\?userId\\=+(\\w)+(&password=)+(\\w)+(&name=)(\\w)+(&email=)(.*%40\\w+\\.com)");
}
