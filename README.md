<div align="center"><h1>
    🤚🏻BAEKER 의 Member Server 입니다.
</h1></div>

<div align="center"><h3>
    회원 정보 관리, social login, jwt 생성을 담당합니다.
</h3></div>

<div align="center"><a href="https://github.com/BAEKER-230522"><b>
    🔗 Organization page
</b></a></div>
<div align="center"><a href="https://github.com/BAEKER-230522/Gateway"><b>
    🔗 Gateway Server
</b></a></div>

<br>

## ERD
- Base Entity 로 부터 중복 필드를 상속받았습니다.
- Meber 의 7일간의 문제 해결 내역을 Snapshot 에서 기록합니다.

<img width="680" alt="스크린샷 2023-10-13 오후 1 47 26" src="https://github.com/BAEKER-230522/Community/assets/115536240/21b53204-e32b-4145-9215-5e32ee359ffa">

<br>

## 핵심 요구사항
### 01 생성
- kakao 소셜 로그인
- jwt 생성
- 리프레시 토큰 생성

<br>

### 02 조회
- id 로 회원 조회
- 백준 연동한 모든 회원 목록 조회
- 모든 회원조회 + 페이징
- 오늘 해결한 문제수 조회
- 7일간 해결한 문제수 조회
- 특정 스터디에 가입한 회원 목록 조회
- 랭킹순으로 회원 목록 조회

<br>

### 03 수정
- 백준 ID 와 연동
- 개인정보 수정
- my study 추가
- 프로필 이미지 관리
- 마지막으로 해결한 문제 id 저장
- 해결한 문제수 최신화
- 랭킹 최신화

<br>

### 04 삭제
- my study 삭제

<br>
