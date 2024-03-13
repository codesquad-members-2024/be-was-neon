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

## 구현 내용
- Thread 대신 ExecutorService 를 사용하도록 리팩토링
- HTTP Request 내용 출력
- 파일 형식에 따라 HTTP Response Header 의 content-type을 변경
- 요청 타켓이 디렉토리면 해당 디렉토리의 `index.html` 파일을 반환하도록 구현
- RequestHandler의 run 메서드의 코드를 HttpRequest, HttpResponse 클래스로 분할

## 요청 타겟 별 기능
- `/` : `index.html` 을 반환
- `/login` : `login/index.html` 반환
- `/registration` : `registration/index.html`
- `/user/create` : 쿼리 파라미터의 값으로 User 객체를 만들어 저장
