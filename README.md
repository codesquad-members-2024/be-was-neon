# be-was-2024
## 구현사항
### step1
**모든 Request Header 출력**
- 일반적으로 헤더는 라인 단위로 구성된다. 
- 라인 단위로 데이터를 읽기 위해 IntStream -> BufferedReader 로 변경

**Header 첫 번째 라인에서 /index.html 추출**
- util 패키지로 분리
- 첫 번재 라인을 공백 기준으로 분리 한뒤, index 1 반환  
 
**/index.html 추출 테스트**
- Header가 GET /index.html HTTP/1.1 형식일 때,
- /index.html 이 잘 추출되는지 확인 

**Java Thread -> Concurrent 리팩토링** 
- 찾아서 하긴 했는데, 아직 왜 Thread 보다 Concurrent 를 사용해야 하는지 등 알지 못함
- 추가 학습 필요 

### step2 
**index.html 에서 회원가입 누르면 회원 가입 폼으로 이동**
- 회원 가입 메뉴에 하이퍼링크 추가 

**회원가입 폼에서 회원 가입 클릭하면 /create?입력값 서버에 전달**
- registration/index.html 수정
  - class="form" action="/create" method="get" 
  - button type="submit" 

**회원가입 폼에 입력한 내용 파싱해서 model.User 클래스에 저장**
- 쿼리 파라미터 존재 여부 확인 후 Map에 저장 
  - 구분자로 구분해 key : value 형태로 저장 
- value 추출해서 User 에 전달 

**RequestLineParser**
- RequestLine 에서 URL 과 QueryParameter 파싱하는 역할 
- 객체 생성 시 RequestLine(firstLine) 받아
  - 쿼리 파라미터 존재 여부 확인 후 적절하게 URL 파싱해서 저장 
- 쿼리 파라미터가 존재한다면
  - 각각의 파라미터를 & 구분자로 구분해
  - = 구분자로 키 밸류 형식으로 분류해 Map에 저장 

## 구현하며 신경 쓴 점
- 생소한 개념이 많아서 자바 스레드 모델, concurrent 패키지 등을 학습했다.
- 동작원리를 파악하는데 시간을 투자했다. 
- 이번 주, Http 추가 학습 예정 

- 우선 클래스를 나누고 하는 것보다는 이해하고 구현하고 나중에 리팩토링하자! 

## 아쉬운 점
- run 메서드가 너무 많은 일을 하고 있어 
  - 가독성도 안좋고
  - 테스트도 어려움... 
  - 리팩토링 하고 싶지만, 우선순위가 뒤로 밀려 하지 못함
  - step3 구현 이후 시도해볼 예정...! 

## 학습
### Java Concurrent 패키지
- java.util.concurrent 는 동시성 프로그래밍을 지원하는 클래스들을 제공한다.
- java.util.concurrent 패키지의 Executor(ExecutorService : Executor 를 상속) 프레임워크를 사용하여 스레드 풀을 관리 할 수 있다.
- execute() vs submit()
  - execute() 메서드는 Runnable 인터페이스만 인자로 받을 수 있습니다.
  - submit() 메서드는 Runnable 인터페이스와 Callable 인터페이스 모두 인자로 받을 수 있습니다.
  - execute() 메서드는 반환타입이 void입니다. 즉, 반환 값이 없습니다.
  - submit() 메서드는 Future 객체를 반환합니다.

### HTTP Request
- 첫 줄은 요청라인 Request Line 이다. 
  - 요청 라인은 공백 기준 메서드, URI, HTTP Version 정보를 담고 있다. 
- 그 다음은 Request Header 
  - 헤더는 대소문자 구분이 없고
  - key-value 형식으로 콜론 (:) 으로 구분 된다. 
- Http Body 
  - 요청의 마지막 부분 

### GET 메서드 - 조회
- 클라 -> 서버로 데이터 전송 
  - 쿼리 파라미터를 통한 전송
    - GET (조회) 사용
    - form의 submit 버튼 누르면 웹 브라우저가 HTTP 요청 메세지 생성
      - form 내용을 쿼리 파라미터로 전달  
  - 메세지 바디를 통한 전송
    - POST (생성), PUT, PATCH (업데이트)
    - form의 submit 버튼 누르면 웹 브라우저가 HTTP 요청 메세지 생성
      - form 내용을 HTTP 메시지 바디 통해 전송 