### 구현 내용 🧑‍💻

- 웹 서버 구현
    - `http://localhost:8080/index.html` 에 대한 요청을 처리합니다.
    - `src/main/resource/static` 디렉토리의 `index.html` 파일을 읽어 응답합니다.

- HTTP Request 내용 출력
    - 적절한 파싱을 통해 요청 내용을 로거(`log.debug`)를 이용하여 출력합니다.

- Thread -> Concurrent 패키지를 사용하도록 변경

### 동작 원리 파악

1. `WebServer` 클래스의 `main` 메서드를 통해 서버 실행이 시작됩니다.
2. 포트번호를 8080으로 초기화합니다.
3. `java.net.ServerSocket` 클래스를 사용하여 서버 소켓을 생성합니다.
    - `new ServerSocket(포트번호)`를 통해 서버 소켓을 생성합니다.
4. `java.net.Socket` 클래스를 사용하여 클라이언트와의 연결을 수락합니다.
5. `ServerSocket`에서 요청이 들어오면 이를 `RequestHandler`에 전달합니다.
6. `RequestHandler`는 `Runnable` 인터페이스를 구현하여 `run()` 메서드를 구현합니다.
7. `getInetAddress()`를 사용하여 연결된 소켓의 원격 호스트의 IP 주소를 출력합니다.
    - `/0:0:0:0:0:0:0:1`은 로컬 호스트의 IPv6 주소를 나타내며, `127.0.0.1`과 동일합니다.
8. `getPort()`를 사용하여 연결된 로컬 포트 번호를 출력합니다.
    - 서버의 포트 번호가 아닌 연결된 포트를 출력하므로, 8080이 아닐 수 있습니다.
9. `Socket`을 `getInputStream()`을 이용하여 입력 스트림을 가져옵니다.
10. `BufferedReader`를 통해 읽어온 내용을 `readLine()`을 사용하여 `/index.html`을 가져옵니다.
11. `Socket`의 `outputStream`을 `DataOutputStream`로 변경합니다.
12. HTML을 바이트 코드로 변환하여 `body`를 통해 클라이언트에게 전송합니다.

### 자세히

`java.net` 패키지는 TCP/IP를 추상화하여 네트워킹을 지원합니다. 네이티브 코드로 구현되어 있어 자바의 소스 코드로 직접 확인할 수 없습니다.

`ServerSocket` 클래스는 `java.io.Closeable` 인터페이스를 구현하고 있습니다.