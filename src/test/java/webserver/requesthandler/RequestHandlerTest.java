package webserver.requesthandler;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.requesthandler.handlerimpl.ArticleHandler;
import webserver.requesthandler.handlerimpl.RequestHandler;
import webserver.requesthandler.http.HttpResponse;

class RequestHandlerTest {

    @Test
    @DisplayName("파라미터로 입력받은 경로의 index.html 파일을 response 의 바디로 설정할 수 있다.")
    void setHTMLToBody() throws IOException {
        RequestHandler requestHandler = new ArticleHandler();
        HttpResponse response = new HttpResponse();
        requestHandler.setHTMLToBody(response, "/article");
        byte[] body = response.getBody();
        assertThat(body).isEqualTo(getExpected());
    }

    private byte[] getExpected() throws IOException {
        return Objects.requireNonNull(getClass().getResourceAsStream("/static/article/index.html"))
                .readAllBytes();
    }
}