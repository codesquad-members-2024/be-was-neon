package webserver.Mapping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpHandler.Mapping.MappingMatcher;
import webserver.HttpMessage.Request;
import webserver.HttpMessage.Response;
import webserver.TestUtils;
import webserver.HttpMessage.constants.eums.ResponseStatus;

import static org.assertj.core.api.Assertions.*;

class MappingMatcherTest {
    @Test
    @DisplayName("resource GET 요청이 들어오면 resourceHandler 의 메서드를 실행해 Response 를 반환한다")
    void getResourceResponse() throws Exception {
        // given
        Request request = TestUtils.getIndexRequest;

        //when
        Response response = new MappingMatcher(request).getResponse();

        //then
        assertThat(response.getStartLine().getStatus()).isEqualTo(ResponseStatus.OK);
        assertThat(response.getHeader().getHeaderFields().containsKey("Content-Length")).isTrue();
    }

    @Test
    @DisplayName("create POST 요청이 들어오면 userHandler 의 메서드를 실행해 Response 를 반환한다")
    void getCreateResponse() throws Exception {
        // given
        Request request = TestUtils.createUserRequest;

        //when
        Response response = new MappingMatcher(request).getResponse();

        //then
        assertThat(response.getStartLine().getStatus()).isEqualTo(ResponseStatus.FOUND);
        assertThat(response.getHeader().getHeaderFields().containsKey("Location")).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 메서드의 요청이 들어오면 예외를 던진다")
    void getInvalidMethodResponse() throws Exception {
        // given
        Request request = new Request("PUT / HTTP/1.1");

        //when
        //then
        assertThatThrownBy(() -> new MappingMatcher(request).getResponse())
                .isInstanceOf(IllegalAccessException.class).hasMessage("설정되어 있지 않은 http 메소드입니다.");
    }
}