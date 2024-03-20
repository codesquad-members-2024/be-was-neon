package http;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieTest {

    @DisplayName("myCookie=MyValue; 에 해당하는 쿠키를 만들 수 있다")
    @Test
    void createCookie() {
        // given
        Cookie cookie = new Cookie("myCookie", "MyValue");

        // when
        String result = cookie.getCookie();

        // then
        assertThat(result).isEqualTo("myCookie=MyValue; ");
    }

    @DisplayName("myCookie=MyValue; Path=/index.html; 에 해당하는 쿠키를 만들 수 있다")
    @Test
    void setPath() {
        // given
        Cookie cookie = new Cookie("myCookie", "MyValue");

        // when
        cookie.setPath("/index.html");
        String result = cookie.getCookie();

        // then
        assertThat(result).isEqualTo("myCookie=MyValue; Path=/index.html; ");
    }

    @DisplayName("myCookie=MyValue; Path=/index.html; Max-Age=1600; 에 해당하는 쿠키를 만들 수 있다")
    @Test
    void setMaxAge() {
        // given
        Cookie cookie = new Cookie("myCookie", "MyValue");

        // when
        cookie.setPath("/index.html");
        cookie.setMaxAge(1600);
        String result = cookie.getCookie();

        // then
        assertThat(result).isEqualTo("myCookie=MyValue; Path=/index.html; Max-Age=1600; ");
    }
}