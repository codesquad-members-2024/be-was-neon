# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

---

# 📖 주간 학습 계획
- ```docs/week1.md``` 에 위치합니다.

# 🖥️ 프로그램 동작

## 포트번호, STATIC 경로 처리
- ```config.yaml```파일을 통해 static source를 불러온다.
- ```config.yaml```파일을 통해 port 번호를 불러온다.

## ```localhost:8080/index.html```
- STATIC/index.html 파일을 불러온다.

### ```회원가입 버튼```을 통해 회원가입 페이지로 이동이 가능하다.

## ```localhost:8080/registration/index.html```
- STATIC/registration/index.html 파일을 불러온다.

### 회원정보를 입력하여 회원가입이 가능하다


## ```localhost:8080/registration/create```
- 입력된 정보값을 바탕으로 User를 생성한다.
- User가 생성되면, main화면으로 돌아간다.


## ```localhost:8080/step3test/index.html```
- step-3의 파일들 로딩을 테스트하기 위한 페이지

- ```jpg```, ```jpeg```, ```png```, ```js```파일을 불러오는 지 확인한다.
  - button을 클릭하여 js파일이 잘 동작하는지 확인한다.