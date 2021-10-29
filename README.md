# 휴가 신청 시스템

## 요구 사항
* 사용 언어: Kotlin
* Java Version: 11
* 애플리케이션 형태: API 서버
* DBMS: postgres

## 실행 방법
아래 명령어를 통해서 빌드를 진행합니다.
```bash
$ ./gradlew bootJar
```

docker 명령어를 통해서 실행합니다.
```bash
$ cd docker
$ docker compose up -d
```

아래 주소를 통해서 API 문서를 확인합니다.
* [localhost:8080/api/docs]()

## 실행 시나리오
**src/http/script.http** 순서대로 실행하면 됩니다.

## API 구현 사항
### 로그인 API
- [X] ```POST api/users/sign-in``` HTTP API를 통해서 로그인을 할 수 있다.
- [X] 해당 API의 Request Body는 다음과 같다.
  ```json
  {
    "id": "user1",
    "password": "1234"
  }
  ```
- [X] 해당 API의 Response Body는 다음과 같다.
  ```json
  {
    "id": "user1"
  }
  ```
- [X] 사용자가 존재하지 않으면 UserNotFoundException이 발생한다.
- [X] 비밀번호가 틀리면 PasswordInvalidException이 발생한다.

### 휴가 목록 조회 API
- [X] ```GET api/vacations?user-id={user-id}&offset-vacation-id={vacation-id}&limit={limit}``` HTTP API를 통해서 연차 목록을 조회할 수 있다.
- [X] 해당 API의 Response Body는 다음과 같다.
  ```json
  {
    "remain_vacation_count": 12.5,
    "vacations":[
      {
        "id": 1,
        "date": "2021-08-01",
        "type": "ANNUAL_LEAVE",
        "comment": "개인 사유로 연차 사용" 
      },
      /// 생략
    ] 
  }
  ```
- [ ] 조회되는 내용이 없으면 빈 배열을 반환한다.

### 휴가 조회 API
- [X] ```GET api/vacations/{vacation-id}``` HTTP API를 통해서 특정 연차를 조회할 수 있다.
- [X] 해당 API의 Response Body는 다음과 같다.
  ```json
  {
    "id": 1,
    "startAt": "2021-08-01",
    "endAt": "2021-08-01",
    "type": "ANNUAL_LEAVE",
    "comment": "개인 사유로 연차 사용" 
  }
  ```
- [X] ID에 해당하는 휴가가 없으면 VacationNotFoundException이 발생한다.

### 휴가 취소 API
- [X] ```DELETE api/vacations/{vacation-id}``` HTTP API를 통해서 특정 연차를 취소할 수 있다.
- [X] ID에 해당하는 휴가가 없으면 VacationNotFoundException이 발생한다.
- [X] 이미 지난 휴가에 대해서 취소를 요청하면 VacationUsedException이 발생한다. (당일 취소 불가)

### 휴가 신청 API
- [X] ```POST api/vacations``` HTTP API를 통해서 연차를 신청할 수 있다.
- [X] 해당 API의 Request Body는 다음과 같다.
  ```json
  {
    "userId": "test",
    "startAt": "2021-08-01",
    "endAt": "2021-08-01",
    "type": "ANNUAL_LEAVE", 
    "useCount": 2,
    "comment": "개인 사유로 연차 사용"
  }
  ```
- [X] 해당 API의 Response Body는 다음과 같다.
  ```json
  {
    "id": 1,
    "remainCount": 1,
    "totalCount": 15
  }
  ```
- [X] 연차는 일별로 저장된다.
- [X] type은 휴가의 종류로 연차(1일), 반차(0.5일), 반반차(0.25일)으로 정의된다.
- [X] 휴가에서 일단위까지만 고려한다. 시간은 고려하지 않는다.
- [X] type을 입력하지 않으면 연차로 분류된다.
- [X] 휴가신청 기간에 공휴일이 존재하면 제외하고 계산된다.

### 버그 수정
- [X] 휴가 취소시 오늘 날짜보다 후인 휴가만 삭제할 수 있도록 변경
- [X] 휴가 목록 조회 남은 휴가 나오지 않도록 변경