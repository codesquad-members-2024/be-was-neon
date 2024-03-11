# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## 학습목표
- 자바 스레드 모델의 버전별 변경
- `concurrent` 패키지
- HTTP Request

## 구현 내용
- Thread 대신 ExecutorService 를 사용하도록 리팩토링
- HTTP Request 내용 출력
- 파일 형식에 따라 HTTP Response Header 의 content-type을 변경
- 요청 타켓이 디렉토리면 해당 디렉토리의 `index.html` 파일을 반환하도록 구현