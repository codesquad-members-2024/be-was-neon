# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

# GitHub Wiki
- 학습 내용 정리: [WAS1 - Wiki](https://github.com/Yeriimii/be-was-neon/wiki/Java-Concurrent-%E2%80%90-CompletableFuture)

# 전체 요청 흐름
```mermaid
---
title: 요청 흐름도 
---
flowchart LR
    subgraph RequestHandler
        direction LR
        subgraph convert
            direction TB
            HttpHeaderParser --Header 정보--> HttpConverter
            HttpConverter --파싱 요청--> HttpHeaderParser
        end
        subgraph mapping
            UriMapper
        end
        subgraph process
            direction TB
            ResourceHandler --byte array--> HttpProcessor
            HttpProcessor --static file path--> ResourceHandler
        end
        HttpRequest
        HttpResponse
    end
WebServer --Socket--> RequestHandler
HttpConverter --> HttpRequest
HttpConverter --> HttpResponse
HttpRequest --URI--> UriMapper --> HttpProcessor --write--> HttpResponse --> Client
```

## HTTP GET 요청 흐름 (`/index.html`, `/registration`)
```mermaid
sequenceDiagram
    actor client
    client->>WebServer: 1. GET 요청: '/index.html'
    activate WebServer
    WebServer->>WebServer: 2. accept --> Socket(connection) 생성 
    deactivate WebServer
    WebServer->>RequestHandler: 3. Socket(connection) 전달
    activate RequestHandler
    RequestHandler->>RequestHandler: 4. HttpConverter --> `HttpRequest`, `HttpResponse` 생성
    RequestHandler->>UriMapper: 5. `HttpRequest`를 처리할 수 있는 `Processor` 전달 요청
    UriMapper-->>RequestHandler: 6. `Processor` 반환 (HttpRequest, HttpResponse 처리)
    RequestHandler->>RequestHandler: 7. `Processor` 로직 실행 (없으면 404 Not Found)
    RequestHandler-->>client: 8. `HttpResponse` 응답
    deactivate RequestHandler
    activate client
    client-->>client: 9. 화면 구성
    deactivate client
```

## 회원가입 흐름(GET 버전)
```mermaid
sequenceDiagram
    actor client
    client->>WebServer: 1. GET 요청: "/registration?id=yelly&password=qwerty"
    WebServer->>RequestHandler: 2. Socket(connection) 전달
    activate RequestHandler
    RequestHandler->>RequestHandler: 3. UriMapper 통해 회원가입 처리할 Processor 찾음
    RequestHandler->>Processor: 4. HttpRequest 처리 요청
    activate Processor
    Processor->>Processor: 5. User 생성
    Processor->>Database: 6. User 등록 요청
    deactivate Processor
    RequestHandler-->>client: 7. HttpResponse 반환 (200 OK (추후 201 created로 변경))
    deactivate RequestHandler
    client->>WebServer: 8. `/registration` 리디렉션 (WebServer에 다시 GET 요청)
    WebServer-->>client: 9. `/registration` 리디렉션 응답(RequestHandler 전달 생략)
```

# 기능 구현 리스트
## RequestHandler
- [x] 요청 헤더(GET/Host/Connection/Accept)에 대해 파싱하고 로그로 출력할 수 있다
- [x] 'localhost:8080/index.html' 요청에 대해 정적 html을 응답할 수 있다

## HttpHeaderParser
- [x] request line, host, connection 등을 파싱할 수 있다
- [x] 쿼리 파라미터를 파싱해서 Map으로 변환할 수 있다

## HttpRequestConverter
- [x] Socket의 connection으로부터 requestString을 한 줄로 만들 수 있다
- [x] 한 줄의 requestString으로부터 HttpRequest 객체로 컨버팅 할 수 있다
- [x] http 요청에 body가 있을 경우 body 내용을 쿼리 파라미터로 가져올 수 있다

## HttpResponseConverter
- [x] Socket의 connection으로부터 HttpResponse를 생성할 수 있다

## ResourceHandler
- [x] 파일을 읽어 byte로 변환할 수 있다
- [x] URI 경로에서 확장자만 추출할 수 있다

## StaticMapper
- [x] 요청 URI에 해당하는 정적 파일을 매핑할 수 있다
- [x] 존재하지 않는 URI는 빈 Optional을 반환할 수 있다
