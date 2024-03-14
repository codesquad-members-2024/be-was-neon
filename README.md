# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## 학습 내용
- `concurrent` 패키지
  - 동시성이 필요한 프로그래밍을 위한 패키지
  - ExecutorService 를 사용하면 스레드를 직접 생성하지 않고 스레드 풀에 작업을 요청할 수 있다.
- HTTP Request
  - 시잘 줄에는 `HTTP 메서드`, `요청 타겟`, `HTTP 버전` 이 존재한다.
  - 이후 HTTP 헤더가 존재한다.
- HTTP Response
  - 상태 줄은 프로토콜 버전, 상태 코드, 상태 텍스트가 존재한다. (`HTTP/1.1 404 Not Found.`)
  - HTTP 헤더가 존재한다.
  - 빈 줄 이후 본문이 위치한다.
- HTTP Status
  - 요청이 성공하면 `200 OK`
  - 리다이렉션
    - 리다이렉션하면 브라우저 URI에 쿼리파라미터가 남지 않게할 수 있다. 
    - `301 Moved Permanently`는 요청 메서드가 GET이 되고, 요청 메시지 바디가 제거될 수 있음
    - `308 Permanent Redirect`는 요청 메서드와 바디가 유지

## 구현 내용
- Thread 대신 ExecutorService 를 사용하도록 리팩토링
- HTTP Request 내용 출력
- 파일 형식에 따라 HTTP Response Header 의 content-type을 변경
- 요청 타켓이 디렉토리면 해당 디렉토리의 `index.html` 파일을 반환하도록 구현
- RequestHandler의 run 메서드의 코드를 HttpRequest, HttpResponse 클래스로 분할
- 회원 가입 기능 구현
  - 쿼리 파라미터를 파싱해 `Map`으로 저장하는 로직 추가
  - 유저 정보가 들어있는 `Map` 객체를 받아 `User` 객체를 반환하는 정적 팩토리 메서드 구현  
  - 회원 가입 시 로그인 페이지로 리다이렉션

## 요청 타겟 별 기능
- `/` : `index.html` 을 반환
- `/login` : `login/index.html` 반환
- `/registration` : `registration/index.html`
- `/create` : 쿼리 파라미터의 값으로 User 객체를 만들어 저장 후 로그인 페이지로 리다이렉트
