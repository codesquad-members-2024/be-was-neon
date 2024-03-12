package utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileManagerTest {

    @Test
    @DisplayName("Request Header 정보를 읽어 url을 추출할 수 있다.")
    void getUrl() {
        String requestHeader = """
                GET /index.html HTTP/1.1
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
                Accept-Encoding: gzip, deflate, br, zstd
                Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7
                Connection: keep-alive
                Cookie: Idea-1a9f010d=715e5261-a9a9-43bf-9fc1-44f77eaa631b; Idea-510107ab=fb62cd23-96bb-4072-a6e0-96eeeafd5830; JSESSIONID=97A1082A2CF7A4A450791B462E96860E
                Host: localhost:8080
                Sec-Fetch-Dest: document
                Sec-Fetch-Mode: navigate
                Sec-Fetch-Site: none
                Sec-Fetch-User: ?1
                Upgrade-Insecure-Requests: 1
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36
                sec-ch-ua: "Chromium";v="122", "Not(A:Brand";v="24", "Google Chrome";v="122"
                sec-ch-ua-mobile: ?0
                sec-ch-ua-platform: "macOS\"""";

        String url = FileManager.getUrl(requestHeader);
        assertThat(url).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("파일 경로에서 확장자를 추출할 수 있다.")
    void getFileExtension() {
        String filePath = "./src/main/resources/static/index.html";
        String fileExtension = FileManager.getFileExtension(filePath);
        assertThat(fileExtension).isEqualTo("html");
    }
}