## Web Server 구현 1
- 웹 서버 1단계 - index.html 응답, 3단계 - 다양한 컨텐츠 타입 지원

### 학습 내용
[소켓, 입출력 스트림, 프로세스와 스레드](https://github.com/sharpie1330/be-was-neon/wiki/%EC%86%8C%EC%BC%93,-%EC%9E%85%EC%B6%9C%EB%A0%A5-%EC%8A%A4%ED%8A%B8%EB%A6%BC,-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%EC%99%80-%EC%8A%A4%EB%A0%88%EB%93%9C)
- New Client Connect! Connected IP : {}, Port : {} 로그 메시지에서 포트 번호가 매번 바뀌는 이유
    - 클라이언트 측에서 요청을 보낼 때 운영체제가 동적으로 포트를 할당해 준다. 요청을 보낸 해당 포트로 응답을 보내면 된다.
    - 서버 측에서는 8080포트로 지정해 주었기 때문에 해당 포트로 항상 listen 중. 이 어플리케이션을 구분하는 포트 번호가 8080이다.

### 구현 내용
- 요청의 첫 줄을 읽으면 GET과 같은 request method와 request url, HTTP/1.1과 같은 프로토콜 순서로 표시된다.
- 따라서 해당 request line의 request url과 method를 파싱해 로거로 출력했다.
- request url에는 클라이언트가 GET을 요청하는 경로가 담겨있고, 해당 파일의 확장자에 따라 ContentType을 다르게 처리해 응답해주어야 한다.
- 우선 해당 경로의 파일을 찾아 바이트 배열로 읽고, 파일의 확장자로 MimeType을 결정한다. 이때 nio 라이브러리를 사용하지 않기 위해 FileInputStream 클래스를 사용해 구현했다.
- MimeType은 enum 클래스로 만들어서 파일의 확장자와 같은 이름의 enum 상수가 있으면 해당 상수의 mimeType값을 구할 수 있도록 했다.

- 웹 서버 2단계 - GET으로 회원가입
### 학습 내용
[[week1] WebServer.java 예제코드 분석](https://github.com/sharpie1330/be-was-neon/wiki/%5Bweek1%5D-WebServer.java-%EC%98%88%EC%A0%9C%EC%BD%94%EB%93%9C-%EB%B6%84%EC%84%9D)

### 구현 내용 및 발생 이슈

[🌐 301 vs 302 상태 코드 차이점 (SEO)](https://inpa.tistory.com/entry/HTTP-🌐-301-vs-302-상태-코드-차이점-💯-완벽-정리)

[HTTP response status codes - HTTP | MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#information_responses)

- **겪은 문제:**
    - /user/create 요청에 대해 처리한 뒤 301 리다이렉트를 응답한다. 첫 번째 사용자 생성 요청에서는 정상적으로 처리되나, 그 이후 다시 요청하면 요청이 처리되지 않는다. 캐시 문제인가 싶어서 캐시를 삭제했더니 성공했다. 왜 그럴까…?


- 301 Moved Permanently
    - 영구 리다이렉션
- 302 Found
    - 일시 리다이렉션

리다이렉션은 페이지의 주소가 바뀌었을 때나 웹사이트를 새로운 도메인으로 옮길 때 이용자들로 하여금 변경된 주소로 자동으로 옮겨가게 만드는 데 사용된다.

이 둘을 구별하는 주체 대상은 → 사람 X ⇒ 검색 엔진, 검색 봇!

301의 경우 말 그대로 요청한 정보(사이트나 페이지)가 영구적으로 옮겨져 다시는 돌아오지 않을 것임을 말해준다.
영원히 이동되었으니, 검색 엔진은 기존 URL에 대한 사이트 랭크와 평가점수와 같은 모든 SEO 값과 링크 리소스를 New URL로 이전, 페이지에 대한 정보도 New URL 페이지만을 수집한다.
포털 검색에 이전 URL을 없애주고 새 URL을 표시하게 해주며, 링크 신호를 통합해주기 때문에 이전 URL의 백링크들을 새 URL를 직접 가리키는 것처럼 통합시켜 준다.

302의 경우 말그대로 요청한 정보가 일시적으로 옮겼다는 것을 말해준다. 즉, 언제든지 이전 URL로 다시 돌아올 수 있다는 뜻. 컨텐츠만 New URL에서 조회하도록 하고, 리다이렉트 했음에도 여천히 Old URL 페이지에 대한 정보를 수집한다.

---

### HttpResponse, HttpHeader 클래스 구현 이야기

Http 응답은 다음과 같은 구조를 가진다. response line, response header, response body. 여기서 response line은 HTTP 버전, 상태 코드로 구성되어 있다.

이 항목들을 필드로 가지는 HttpResponse 클래스를 구현하고자 했다.

> 여기서 문제… 헤더 값의 개수는 정말 많고, response header나 body가 없어도 브라우저는 응답만 보고 인식할 수는 있다!

header의 경우 하나의 키에 하나의 값만 존재하지 않기에 value가 List<String> 형태가 되어야 한다.

또 나는 하나의 키와 하나의 값만 인자로 받는 메서드를 만들어 header 맵에 추가할 수 있도록 하고 싶었고, 생성할 때부터 착 착 착 조립해서 HttpResponse를 만들어 내고 싶었다!

⇒ 그래서 **빌더 패턴**을 사용했다.

스프링 부트 프레임워크의 ResponseEntity 클래스를 보고 공부하며 구현했다.

```java
public interface Builder{
        Builder header(String headerName, String... headerValues);

        Builder headers(HttpHeader headers);

        Builder contentLength(long contentLength);

        Builder contentType(MIMEType mimeType);

        Builder location(String location);

        HttpResponse body(byte[] body);

        HttpResponse build();
    }
```

클래스 내부에 다음과 같은 인터페이스를 만들고, ResponseBuilder라는 정적 내부 클래스를 만들어 위 인터페이스를 구현했다.
빌더 자기 자신을 응답하고, 마지막 build()나 body()에서 HttpResponse를 반환하므로 메서드 체이닝으로 필드를 채울 수 있고, 최종적으로 객체를 완성할 수 있다.


⇒ 다음 문제. **너무 복잡한 헤더 필드 구조**

이걸 전부 구현할 때까지만 해도 Response의 헤더 필드는 Map<String, List<String>> 형태였다.
HttpHeader라는 새로운 클래스로 분리하고 싶었다.

따라서 Map<String, List<String>> 형태를 implements 하도록 하고, 또 Map<String, List<String>> 형태의 headers라는 필드를 private final로 갖는다. 불변을 보장하는 일급 컬렉션의 형태를 취한다.

인스턴스를 생성하면 headers를 HashMap을 구현 객체로 생성하고, Override한 메서드도 HashMap의 구현 메서드를 활용해서 구현했다.

또 HttpHeader를 생성했으니, 해당 클래스의 메서드를 활용하도록 HttpResponse 클래스 내용도 변경했다.

---
- 아쉬운 점
    - 빌더 패턴으로 객체를 만들 때 기본 헤더 값이 추가되도록 구현하고 싶었는데, 구현하지 못했다.
    - 따라서 필수 헤더 값이 없어도 객체는 생성이 되어 문제 없이 코드가 돌아가는 문제가 남아 있다. 개발자 측면에서 주의해야 한다.
    - HttpHeader 클래스 또한 개발자 측면에서 사용에 주의해햐 하는 메서드들이 많이 존재한다.
---
### Route 객체를 싱글톤 객체로

Route객체는 HttpRequest의 URL path를 구해서 필드로 선언된 맵에 해당 path와 일치하는 키가 있는지 찾고, 있다면 값으로 매핑된 메서드를 실행하고 HttpResponse 객체를 반환하는 역할을 한다.

처음에는 모두 static 메서드로 구성되어 있었는데, 해당 클래스가 util의 역할을 하는 것은 아니었기 때문에 인스턴스 클래스로 변경했다.

하지만 해당 클래스는 소켓이 생성되어 HTTP 요청을 받을 때마다 인스턴스가 생성될 필요는 없었다. 자원의 낭비라는 생각이 들어 싱글톤 클래스로 변경했다.

---

## 3-Way-HandShaking

```java
14:53:10.846 [DEBUG] [pool-1-thread-2] [webserver.request.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 50881
14:53:10.846 [DEBUG] [pool-1-thread-1] [webserver.request.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 50880
14:53:10.848 [ERROR] [pool-1-thread-1] [webserver.request.RequestHandler] - Cannot invoke "String.isEmpty()" because the return value of "java.io.BufferedReader.readLine()" is null
14:53:10.850 [DEBUG] [pool-1-thread-2] [webserver.request.RequestHandler] - request method : POST, request url : /user/create?userId=hello&email=hello%40gmail.com&nickname=hello&password=1234
14:53:10.858 [INFO ] [pool-1-thread-2] [webserver.response.ResponseHandler] - user created successfully! - userId : hello
14:53:10.866 [DEBUG] [pool-1-thread-2] [webserver.request.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 50882
14:53:10.868 [DEBUG] [pool-1-thread-1] [webserver.request.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 50883
14:53:10.870 [DEBUG] [pool-1-thread-1] [webserver.request.RequestHandler] - request method : GET, request url : /registration/welcome.html
14:53:10.870 [ERROR] [pool-1-thread-2] [webserver.request.RequestHandler] - Cannot invoke "String.isEmpty()" because the return value of "java.io.BufferedReader.readLine()" is null
```

RequestHandler의 run()코드에서 NPE 오류가 발생한다!

- 왜 그럴까?

→ 50881 포트의 소켓 연결은 3-way-handshaking을 위한 연결로, 메시지가 전송되어 들어오지 않는다. 코드에서는 BufferedReader로 무조건 첫 한줄을 읽어 request line으로 처리하는데, 이 값이 null이 되어 문제가 발생한 것이다.

⇒ 다음 단계에서는 HTTP 요청이 맞는지 검증하는 로직을 첨부하자!