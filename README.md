# 🌱 be-was-2024

코드스쿼드 백엔드 교육용 WAS 2024 개정판

<br>

# 웹 서버 1단계 - index.html 응답

## ✅ 체크리스트

- [X] 정적인 html 파일 응답(http://localhost:8080/index.html)
- [X] HTTP Request 내용 출력
- [X] 파일의 내용을 바이트 배열로 반환하는 기능을 JDK의 nio를 사용하지 않고 구현
- [X] Java Thread 기반의 기존 프로젝트를 Concurrent 패키지를 사용하도록 변경
- [X] URl 추출 기능 FileExtractorTest 클래스의 static 메소드로 분리
- [X] FileExtractorTest 클래스 Test 추가
- [X] 코드로 표현할 수 없었던 내용은 주석 달기

<br><br>

# 웹 서버 2,3단계 - GET으로 회원가입, 다양한 컨텐츠 타입 지원

## ✅ 체크리스트

- [X] 문자열 디코딩 기능 구현
- [X] 사용자가 입력한 값을 검증하고 잘못된 값을 입력하면 서버에 전달하지 않고 오류 팝업창을 띄운다.
- [X] 사용자가 입력한 값을 파싱해 model.User 클래스에 저장
- [X] 가입 버튼을 클릭하여 사용자가 입력한 값을 서버에 전달
- [X] model.User 클래스에 정상적으로 저장되었다면 User의 `toString()`메소드를 사용하여 생성된 결과를 출력한다.
- [X] 다양한 컨텐츠 타입 지원

<br><br><br>

## 이렇게 구현했어요 🤗

# 웹 서버 1단계 - index.html 응답

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

<br><br><br>

# 웹 서버 2,3단계 - GET으로 회원가입, 다양한 컨텐츠 타입 지원

### 1️⃣ 사용자가 작성한 내용 검증 후 서버에 전송
```javascript
document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 유효성 검사를 위해 폼 제출 이벤트의 기본 동작을 막음
    if (validateForm()) {
        var id = document.getElementById("userId").value;
        var password = document.getElementById("userPw").value;
        var name = document.getElementById("userName").value;
        var email = document.getElementById("userEmail").value;
        var url = '/user/create?userId=' + id + '&userPw=' + password + '&userName=' + name + '&userEmail=' + email;

        fetch(url)
            .then(response => {
                if (response.ok) {
                    alert('회원가입 성공!');
                    document.getElementById('signupForm').reset(); // 폼 초기화
                } else {
                    alert('회원가입에 실패');
                }
                id.value = null;
                password.value = null;
                name.value = null;
                email.value = null;
            })
            .catch(error => {
                console.error('[Error] 에러 발생 :', error);
            });
    }
});

function validateForm() {
    var userId = document.getElementById("userId").value;
    var nameRegex = /^[a-zA-Z0-9]+$/;
    if (!nameRegex.test(userId)) { // 아이디가 영어와 숫자로만 되어 있는지 검증한다.
        alert("아이디는 영어와 숫자만 입력할 수 있습니다.");
        return false;
    }
     
    var userPw = document.getElementById("userPw").value;
    if (userPw.length < 8) { // 비밀번호가 8자 이상인지 검증한다.
        alert("비밀번호는 8자 이상이어야 합니다.");
        return false;
    }

    var userName = document.getElementById("userName").value;
    var nameRegex = /^[a-zA-Z가-힣]+$/;
    if (!nameRegex.test(userName)) { // 이름이 영어와 한글로만 되어 있는지 검증한다.
        alert("이름은 영어와 한글만 입력할 수 있습니다.");
        return false;
    }

    var userEmail = document.getElementById("userEmail").value;
    var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(userEmail)) { // 올바른 이메일 형식인지 검증한다.
        alert("올바른 이메일 형식을 입력해주세요.");
        return false;
    }
    return true; // 모든 유효성 검사를 통과
}
```

사용자가 입력한 아이디, 비밀번호, 이름, 이메일 값이 각각의 형식에 맞는지 검증한다. <br>
만약 사용자가 잘못된 값을 입력했다면 서버에 전달하지 않고 오류 팝업창을 띄운다.

<br><br>

### 2️⃣ 사용자가 입력한 값을 파싱해 model.User 클래스에 저장

```java
private void createUser(String[] userEncodedInfo)
            throws IndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
    String[] userInfo = Decoder.decodeUser(userEncodedInfo);
    String id = userInfo[ID_INDEX];
    String pw = userInfo[PW_INDEX];
    String name = userInfo[NAME_INDEX];
    String email = userInfo[EMAIL_INDEX];
    User user = new User(id, pw, name, email);
    users.add(user);
    logger.debug(user.toString());
}
```

사용자가 입력한 값에서 아이디, 비밀번호, 이름, 이메일 값만 파싱하여 디코딩한 후 User 객체를 생성하여 임시로 users List에 추가하였다. <br>
또한, User가 올바르게 생성되었는지 확인하기 위해 `logger.debug(user.toString());`를 통해 로그를 출력하도록 하였다.

<br><br>

### 3️⃣ 다양한 컨텐츠 타입 지원

```java
private String getContentType(String filePath) {
    List<String> contentTypes = List.of(ContentType.html.getContentType(), ContentType.css.getContentType(), ContentType.js.getContentType());
    Path path = Paths.get(filePath);
    String contentType = null;
    try {
        contentType = Files.probeContentType(path);
        if (contentTypes.contains(contentType)) {
            return contentType + CONTENT_TYPE_CHARSET; // utf-8 charset 추가
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return contentType;
}
```

각각의 파일에 맞는 컨텐츠 타입을 헤더에 넣어서 전달하기 위해 컨텐츠 타입을 구하는 함수를 활용하였다. <br>