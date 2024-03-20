# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판
# Step4
## 추가 및 변경 내용
- html 파일의 get 으로 request 받아오던 것을 post  로 변경
- request 를 처리하는 메서드에 header 부분의 Content-Length 를 이용하여  
  requestBody 를 읽어오는 메서드를 추가
- 전체적인 구조의 변경 및 클래스, 변수, 메서드 이름들의 수정

## 공부한 내용
### Get 과 Post 의 차이점
#### GET
- GET method 는 클라이언트에서 서버로 어떠한 리소스로부터 정보를 요청하기 위해 사용되는 메서드입니다.
- 데이터를 읽거나(Read),검색(Retrieve)할 때에 사용할수있습니다.   
  GET은 요청을 전송할 때 URL 주소 끝에 파라미터로 포함되어 전송되며, 이 부분을 쿼리 스트링(QueryString)이라고 부릅니다.
- GET은 불필요한 요청을 제한하기 위해 요청이 캐시 될 수 있습니다. 
- 파라미터에 내용이 노출되기 때문에 민감한 데이터를 다룰 때 GET 요청을 사용해서는 안 됩니다. 
- GET 요청은 브라우저 기록에 남습니다. 
- GET 요청을 북마크에 추가할 수 있습니다. 
- GET 요청에는 데이터 길이에 대한 제한이 있습니다.

#### POST
- POST method 는 리소스를 생성/업데이트하기 위해 서버에 데이터를 보내는 데 사용됩니다.
- GET 과 달리 전송해야 될 데이터를 HTTP 메시지의 Body 에 담아서 전송합니다.   
  그리고 그 Body 의 타입은 요청 헤더의 Content-Type 에 요청 데이터의 타입을 표시 따라 결정됩니다. (POST 로 요청을 보낼 때는 해야 합니다.)   
  HTTP 메시지의 Body 는 길이의 제한 없이 데이터를 전송할 수 있습니다. 그래서 POST 요청은 GET 과 달리 대용량 데이터를 전송할 수 있습니다.
- POST 요청은 캐시 되지 않습니다.
- POST 요청은 브라우저 기록에 남아 있지 않습니다.
- POST 요청을 북마크에 추가할 수 없습니다.
- POST 요청에는 데이터 길이에 대한 제한이 없습니다.

#### 차이점
- GET 은 Idempotent, POST 는 Non-idempotent 하게 설계 되었다. Idempotent(멱등)은 수학적 개념인데, 다음과 같이 설명할 수 있다.
  - 수학이나 전산학에서 연산의 한 성질을 나타내는 것으로, 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질.
- GET 이 Idempotent 하도록 설계되었다는 것은 GET 으로 서버에 동일한 요청을 여러 번 전송해도, 동일한 응답이 돌아온다.
  - 주로 데이터를 조회할 때 사용
- POST는 Non-idempotent하기 때문에, 서버에 동일한 요청을 여러 번 전송해도 각기 다른 응답을 받을 수 있다.
  - POST는 서버의 상태나 데이터를 변경시킬 때 사용 (생성에 주로 사용된다.)

출처: https://han-joon-hyeok.github.io/posts/Comparison-of-GET-POST/ [한준혁, Dev Joon:GitHub.io]
출처: https://whales.tistory.com/120 [허도경, limewhale:티스토리]
