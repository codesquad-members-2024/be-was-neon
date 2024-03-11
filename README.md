# 🌱 be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

<br>

#  웹 서버 1단계 - index.html 응답
## ✅ 체크리스트
- [X] 정적인 html 파일 응답(http://localhost:8080/index.html)
- [X] HTTP Request 내용 출력
- [X] 파일의 내용을 바이트 배열로 반환하는 기능을 JDK의 nio를 사용하지 않고 구현
- [X] Java Thread 기반의 기존 프로젝트를 Concurrent 패키지를 사용하도록 변경
- [X] URl 추출 기능 FileExtractorTest 클래스의 static 메소드로 분리
- [X] FileExtractorTest 클래스 Test 추가
- [X] 코드로 표현할 수 없었던 내용은 주석 달기

<br><br><br>


## 이렇게 구현했어요 🤗
### 1️⃣ 정적인 html 파일 응답
```java
private static final String BLANK = "\s+";
private static final String BASE_PATH = "./src/main/resources/static";
private static final int URL_INDEX = 1;

try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    String filePath = br.readLine();

    logger.debug("request line : {}", filePath);
    String url = filePath.split(BLANK)[URL_INDEX];
    byte[] body = readAllBytes(BASE_PATH + url);

    DataOutputStream dos = new DataOutputStream(out);
    response200Header(dos, body.length);
    responseBody(dos, body);
} catch (IOException e) {
    logger.error(e.getMessage());
}
```

`BufferedReader.readLine()` 메소드 활용해 라인별로 http header를 읽고, 적절하게 파싱해서 **로거(log.debug)** 를 이용해 출력한다. <br>
그리고 파일의 내용을 바이트 배열로 만들어 요청 URL에 해당하는 파일을 읽어 전달한다.

<br><br>

### 2️⃣ 파일의 내용을 바이트 배열로 반환하는 기능 구현
```java
private byte[] readAllBytes(String filePath) throws IOException {
    File file = new File(filePath);
    try (FileInputStream inputStream = new FileInputStream(file)) {
        byte[] buffer = new byte[(int) file.length()];
        int bytesRead = inputStream.read(buffer); // 파일 내용 읽기

        if (bytesRead != file.length()) {
        throw new IOException();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```
*JDK의 nio는 사용하지 않는다*라는 제한 사항이 있어 **RequestHandler**의 내부 메소드로 파일의 내용을 바이트 배열로 변환해주는 메소드를 따로 구현하였다.

<br><br>

### 3️⃣ 프로젝트를 Concurrent 패키지를 사용하도록 변경

```java
// 클라이언트가 연결될때까지 대기한다.
Socket connection;
while ((connection = listenSocket.accept()) != null) {
    CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new RequestHandler(connection));
    completableFuture.get();
}
```

**Java Thread** 기반의 기존 프로젝트를 **CompletableFuture**를 사용하는 구조로 변경하였다.