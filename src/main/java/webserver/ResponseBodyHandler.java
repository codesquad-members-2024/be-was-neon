package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMethods;
import utils.Path;

public class ResponseBodyHandler {
    /**
     * Response 객체의 body 멤버변수를 위해 Request의 resource가 명령어인지 아니면 파일 요청인지를 처리해서 적절한 body를 만드는 것이 이 객체의 역할이다.
     */
    private static final Logger logger = LoggerFactory.getLogger(ResponseBodyHandler.class);
    private final Path path;
    private final Request request;
    private Map<String, Runnable> functionMap;
    private String fileUrl;

    public ResponseBodyHandler(Request request) throws IOException {
        this.request = request;
        path = new Path();
        initFunctionMap();
    }

    private void initFunctionMap() {
        functionMap = new HashMap<>();
        functionMap.put("/create", new CreateHandler(request)::create);
    }

    /**
     * body를 어떤 방법으로 처리할지 Method를 통해 분기를 나눈 후 그에 맞게 처리한다.
     *
     * @return
     * @throws IOException
     */
    public byte[] bodyProcessing() throws IOException {
        if (request.getHttpMethod().equals(HttpMethods.GET)) {
            return getProcess();
        }
        if (request.getHttpMethod().equals(HttpMethods.POST)) {
            return postProcess();
        }
        return new byte[0];
    }

    /**
     * GET method인 경우의 처리
     *
     * @return
     * @throws IOException
     */
    private byte[] getProcess() throws IOException {
        fileUrl = path.buildURL(request.getResource());
        return parseFileToByte();
    }

    /**
     * POST method인 경우의 처리
     * @return
     */
    private byte[] postProcess() {
        if (isCreate()) {
            functionMap.get(request.getResource()).run();
        }
        fileUrl = path.buildForRedirection("");
        return new byte[0];
    }

    /**
     * 해당 요청이 create인지 확인한다
     * @return
     */
    private boolean isCreate() {
        final String CREATE_COMMAND = "/create";
        return request.getResource().equals(CREATE_COMMAND);
    }

    /**
     * 응답할 파일 읽어와서 byte[]로 반환
     * @return
     * @throws IOException
     */
    private byte[] parseFileToByte() throws IOException {
        File file = new File(fileUrl);
        byte[] result = new byte[(int) file.length()];
        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(result);
        return result;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
