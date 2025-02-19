# KoreaIT 팀 프로젝트

- 팀명: 한계돌파
- 기간: 25.01.08 - 25.02.12

---
## 브랜치
- **Master**: For Docker + RaspPi Server(1주일 오픈(25.02.19 까지))
- **Dev_Ver**: Eclipse + Maven 버전

링크 -> http://kimec995.iptime.org:20252

- 그 외: 개발 버전에 따름

- Dev_Ver 설정할 때 필수
    - `application.properties` 설정
        - 로그인 / 로그아웃 관련 키 & URL(카카오)
        - tmp 파일 경로
        - DB 서버 관련
    - 지도 관련 키(카카오)
        - `MapService.java` 29번 줄
        - `mapLocation.html` 13번 줄
    - 업로드 파일 경로(Tmp 파일 경로)
        - `UploadController.java` 40, 139번
            - Windows: `C:\Users\<사용자명>\AppData\Local\Temp`
            - Linux: `/tmp/<생성디렉토리>` / `/var/tmp`(찐 저장용)
            - Mac: `/tmp` / `/var/folders/`

---

## 팀 자료
- [노션](https://www.notion.so/1755e2e581d681d881c3e90fee88e16b)
- PPT: `발표` 디렉토리 내 존재
- 로고 및 디자인 자료: 상동
- 로직: [링크](https://drive.google.com/file/d/1dMtot9WwY92goMBYE-9Rx12_vqZ2RWie/view?usp=sharing)

---
## 개요
### 팀원(**가나다**)
- 김은채
    - 메인, 로그인 & 로그아웃, 대시보드, 지도, 서버
    - https://github.com/KimEC995
- 신웅규
    - 자료 조사
    - http://github.com/woongkyu1182
- 정다훈
    - 게시판, 로그인, DB, CSS
    - https://github.com/Dhchung12
- 한수란
    - 문서 정리, CSS
    - https://github.com/suran95
- 홍주희
    - 브랜딩, 대시보드, PT 기획 및 제작, 화면 설계, CSS
    - https://github.com/kittyjh19

### 프로젝트
- 기간: 25년 01월 08일 - 02월 11일(주말 제외 총 20일)
- 기획 배경: 클라이머용 정보 및 커뮤니티 사이트 필요

### 기술
- 협업 기술
    - github(코드 공유)
    - Draw.io(로직 설계)
    - Figma(화면 설계)
    - notion(자료 공유)
    - Discord(소통)
- 기술 및 API
    - Kakao API
        - 지도
        - 로그인
    - Google Chart: 대시보드
    - Thymeleaf: 프론트
    - Spring Boot: 프레임 워크
    - Maven: 서버
    - JDK 21: 언어
    - Maria DB: DB
    - MyBatis + JPA: ORM
    - Docker + Compose: 배포

---
## 개선점
### 코드 최적화
전체적인 코드 최적화 필요.

### 기능(추가 중)
- 커뮤니티 보드
    - 비로그인 사용자도 메인 페이지로 넘어갈 수 있도록.
    - 댓글 적용 시간을 한국에 맞춰서 적용.
- 지도
    - 아이콘 적용
    - 지도가 너무 큼
- 대시보드
    - 처음 선택할 때 날짜 적용