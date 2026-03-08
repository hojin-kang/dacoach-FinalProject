## ⚙️ Application Properties 설정

이 프로젝트는 `src/main/resources/application.properties` 파일을 직접 생성하여 환경에 맞게 설정해야 합니다.

> ⚠️ **주의:** `application.properties`는 보안상의 이유로 `.gitignore`에 등록되어 있습니다.  
> 아래 내용을 참고하여 로컬 환경에 맞게 직접 작성해주세요.

---

### 📄 설정 예시

```properties
# ── Application ──────────────────────────────────────────
spring.application.name=dacoach
server.port=9090

# ── Oracle Database ───────────────────────────────────────
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# ── Multipart (파일 업로드) ────────────────────────────────
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.file-size-threshold=0
spring.servlet.multipart.resolve-lazily=false

# ── Tomcat ────────────────────────────────────────────────
server.tomcat.max-parameter-count=1000
server.tomcat.max-http-form-post-size=50MB
server.tomcat.max-swallow-size=100MB
server.tomcat.max-part-count=-1

# ── Gmail SMTP (메일 발송) ─────────────────────────────────
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

---

### 📌 Gmail 앱 비밀번호 발급 방법

1. [Google 계정 보안 설정](https://myaccount.google.com/security) 접속
2. **2단계 인증** 활성화
3. **앱 비밀번호** 생성 → 앱: `메일`, 기기: `Windows 컴퓨터` (또는 기타)
4. 생성된 16자리 비밀번호를 `spring.mail.password`에 입력

---
<details>
<summary>📄 DB 테이블</summary>
  
```
-- ============================================
--  1. 시퀀스 생성
-- ============================================
CREATE SEQUENCE SEQ_USERS;
CREATE SEQUENCE SEQ_COACH;
CREATE SEQUENCE SEQ_COMPANY;
CREATE SEQUENCE SEQ_MAJOR_FIELD;
CREATE SEQUENCE SEQ_MINOR_FIELD;
CREATE SEQUENCE SEQ_HASHTAG;
CREATE SEQUENCE SEQ_MAJOR_REGION;
CREATE SEQUENCE SEQ_MINOR_REGION;
CREATE SEQUENCE SEQ_REVIEW_COACH;
CREATE SEQUENCE SEQ_REVIEW_CLASS;
CREATE SEQUENCE SEQ_REVIEW_TAG;
CREATE SEQUENCE SEQ_TOKEN;
CREATE SEQUENCE SEQ_CHAT_ROOM;
CREATE SEQUENCE SEQ_NOTIFICATION;
CREATE SEQUENCE SEQ_AGREEMENT;
CREATE SEQUENCE SEQ_SCHEDULE;
CREATE SEQUENCE SEQ_CLASS;
CREATE SEQUENCE SEQ_CLASS_ENROLLMENT;
CREATE SEQUENCE SEQ_PAY;
CREATE SEQUENCE SEQ_TOKEN_HISTORY;
CREATE SEQUENCE SEQ_REPORT;
CREATE SEQUENCE SEQ_LIKES_USER;
CREATE SEQUENCE SEQ_LIKES_CLASS;
CREATE SEQUENCE SEQ_QNA;
CREATE SEQUENCE SEQ_QNA_A;
CREATE SEQUENCE SEQ_MEMBERSHIP;
CREATE SEQUENCE SEQ_MEMBERSHIP_DETAIL;
CREATE SEQUENCE SEQ_AD;
CREATE SEQUENCE SEQ_REASON_TYPE;
CREATE SEQUENCE SEQ_REASON_LOG;
CREATE SEQUENCE SEQ_EMBED_USER;
CREATE SEQUENCE SEQ_RANK;
CREATE SEQUENCE SEQ_CERT;
CREATE SEQUENCE SEQ_KEYWORD;
CREATE SEQUENCE SEQ_MATCH;
CREATE SEQUENCE SEQ_CHALLENGE;

-- ============================================
--  2. 통합 유저 테이블
-- ============================================
CREATE TABLE USERS (
    USER_IDX  NUMBER        PRIMARY KEY,             -- 유저 인덱스 (PK)
    USER_TYPE  VARCHAR2(20)  NOT NULL,                -- 유저 타입 (COACH/COMPANY/ADMIN)
    USER_NAME  VARCHAR2(50)  NOT NULL,                -- 실제 명칭(코치 실명/회사명)
    LOGIN_ID   VARCHAR2(100) NOT NULL UNIQUE,         -- 유저 아이디 (로그인용)
    PASSWORD   VARCHAR2(255) NOT NULL,                -- 비밀번호 (해시 저장)
    STATUS     VARCHAR2(20),                          -- 계정 상태 (INACTIVE (대기) / ACTIVE (사용) / WARNING (주의) / DANGER (위험) / SUSPENDED(정지))
    CREATED_AT DATE                                    -- 가입일
);

-- ============================================
--  3. 분야 대분류
-- ============================================
CREATE TABLE MAJOR_FIELD (
    MAJOR_FIELD_IDX NUMBER        PRIMARY KEY,        -- 분야 대분류 인덱스 (PK)
    MAJOR_FIELD_CD  VARCHAR2(100) NOT NULL UNIQUE     -- 대분류 코드 (스포츠, 음악, 어학 등)
);

-- ============================================
--  4. 분야 소분류
-- ============================================
CREATE TABLE MINOR_FIELD (
    MINOR_FIELD_IDX NUMBER        PRIMARY KEY,        -- 분야 소분류 인덱스 (PK)
    MAJOR_FIELD_IDX NUMBER        NOT NULL,           -- 분야 대분류 인덱스 (FK)
    MINOR_FIELD_NM  VARCHAR2(100) NOT NULL,           -- 소분류 이름 (요가, 필라테스 등)
    CONSTRAINT FK_MINORF_MAJORF FOREIGN KEY (MAJOR_FIELD_IDX) 
        REFERENCES MAJOR_FIELD(MAJOR_FIELD_IDX) ON DELETE CASCADE,
    CONSTRAINT UK_MINOR_FIELD UNIQUE (MAJOR_FIELD_IDX, MINOR_FIELD_NM)
);

-- ============================================
--  5. 해시태그
-- ============================================
CREATE TABLE HASHTAG (
    HASHTAG_IDX     NUMBER        PRIMARY KEY,        -- 해시태그 인덱스 (PK)
    TAGS            VARCHAR2(100) NOT NULL           -- 해시태그 내용
);

-- ============================================
--  6. 지역 대분류
-- ============================================
CREATE TABLE MAJOR_REGION (
    MAJOR_REGION_IDX NUMBER        PRIMARY KEY,       -- 지역 대분류 인덱스 (PK)
    REGION_NM        VARCHAR2(100) NOT NULL UNIQUE    -- 지역명 (서울특별시, 경기도 등)
);

-- ============================================
--  7. 지역 소분류
-- ============================================
CREATE TABLE MINOR_REGION (
    MINOR_REGION_IDX NUMBER        PRIMARY KEY,       -- 지역 소분류 인덱스 (PK)
    MAJOR_REGION_IDX NUMBER        NOT NULL,          -- 지역 대분류 인덱스 (FK)
    DISTRICT_NM      VARCHAR2(100) NOT NULL,          -- 구/군 이름 (강남구, 서초구 등)
    CONSTRAINT FK_MINORR_MAJORR FOREIGN KEY (MAJOR_REGION_IDX) 
        REFERENCES MAJOR_REGION(MAJOR_REGION_IDX) ON DELETE CASCADE,
    CONSTRAINT UK_MINOR_REGION UNIQUE (MAJOR_REGION_IDX, DISTRICT_NM)
);

-- ============================================
--  8. 멤버십 상세 테이블
-- ============================================
CREATE TABLE MEMBERSHIP_DETAIL (
    MEMBER_DETAIL_IDX NUMBER        PRIMARY KEY,      -- 멤버십 상품 인덱스 (PK)
    MEMBER_TYPE       VARCHAR2(40)  NOT NULL,         -- 멤버십 타입 (PREMIUM/NORMAL)
    COST              NUMBER,                         -- 금액
    BENEFIT           VARCHAR2(500),                  -- 혜택(보류)
    CLASS_MAX_CNT     NUMBER                          -- 최대 클래스 가능 개수
);

-- ============================================
--  9. 코치 상세 정보
-- ============================================
CREATE TABLE COACH (
    COACH_IDX     NUMBER        PRIMARY KEY,          -- 코치 인덱스 (PK)
    USER_IDX     NUMBER        NOT NULL UNIQUE,      -- 유저 인덱스 (FK)
    BIRTH_DATE    DATE,                               -- 생년월일(yymmdd) 
    NICKNAME      VARCHAR2(100) UNIQUE NOT NULL,      -- 닉네임 (중복 불가)
    PHONE         VARCHAR2(20),      -- 전화번호 
    MAIL 		VARCHAR2(100) UNIQUE NOT NULL,	-- 이메일(인증용, 중복 불가)
    INTRO         VARCHAR2(3000),                     -- 소개글
    PHOTO         VARCHAR2(500), 				 -- 프로필 사진 경로
    VIDEO         VARCHAR2(500),                      -- 소개 영상 경로
    POINT_SCORE   NUMBER,                             -- 도전과제 점수
    TOKEN_BALANCE NUMBER DEFAULT 0,                   -- 보유 토큰 수
    KAKAO_KEY     VARCHAR2(100) UNIQUE,                      -- 카카오 로그인 고유 ID
    CONSTRAINT FK_COACH_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 10. 컴퍼니 상세 정보
-- ============================================
CREATE TABLE COMPANY (
    COMPANY_IDX NUMBER        PRIMARY KEY,            -- 컴퍼니 인덱스 (PK)
    USER_IDX   NUMBER        NOT NULL UNIQUE,        -- 유저 인덱스 (FK)
    COMPANY_NUM VARCHAR2(30), 	-- 사업자번호
    PHONE       VARCHAR2(20)  UNIQUE NOT NULL,        -- 전화번호 (인증용, 중복 불가)
    ADDRESS     VARCHAR2(500) NOT NULL,               -- 사업장 주소
    EMAIL        VARCHAR2(50),				-- 이메일
    INTRO       VARCHAR2(3000),                       -- 회사 소개글
    PHOTO       VARCHAR2(500), 			-- 회사 로고
    KAKAO_KEY   VARCHAR2(100),                        -- 카카오 로그인 고유 ID
    CONSTRAINT FK_COMPANY_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 11. 정지 유저 테이블
-- ============================================
CREATE TABLE EMBEDDED_USER (
    EMBED_USER_IDX NUMBER       PRIMARY KEY,          -- 정지 내역 인덱스 (PK)
    USER_IDX      NUMBER       NOT NULL,      -- 유저 인덱스 (FK)
    START_DATE     DATE,                              -- 정지 시작일
    END_DATE       DATE,                              -- 정지 해제일
    REASON         VARCHAR2(500),                     -- 정지 사유
    CONSTRAINT FK_EMBED_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 12. 멤버십 - 유저 매핑 테이블
-- ============================================
CREATE TABLE MEMBERSHIP (
    MEMBER_IDX        NUMBER       PRIMARY KEY,       -- 유저 멤버십 인덱스 (PK)
    USER_IDX         NUMBER       NOT NULL,          -- 유저 인덱스 (FK)
    MEMBER_DETAIL_IDX NUMBER       NOT NULL,          -- 멤버십 상품 인덱스 (FK)
    START_DATE        DATE         NOT NULL,          -- 시작 일시
    END_DATE          DATE,                           -- 종료 일시
    STATUS            VARCHAR2(20),                   -- 멤버십 상태 (BEING/STOP)
    CONSTRAINT FK_MEMBER_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_MEMBER_DETAIL FOREIGN KEY (MEMBER_DETAIL_IDX) 
        REFERENCES MEMBERSHIP_DETAIL(MEMBER_DETAIL_IDX) ON DELETE CASCADE
);

-- ============================================
-- 13. 코치 제공 분야 (다대다 매핑)
-- ============================================
CREATE TABLE COACH_PROVIDE (
    COACH_IDX       NUMBER NOT NULL,                  -- 코치 인덱스 (FK)
    MINOR_FIELD_IDX NUMBER NOT NULL,                  -- 분야 소분류 인덱스 (FK)
    CONSTRAINT PK_COACH_PROVIDE PRIMARY KEY (COACH_IDX, MINOR_FIELD_IDX),
    CONSTRAINT FK_CPROV_COACH FOREIGN KEY (COACH_IDX) 
        REFERENCES COACH(COACH_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CPROV_MINORF FOREIGN KEY (MINOR_FIELD_IDX) 
        REFERENCES MINOR_FIELD(MINOR_FIELD_IDX) ON DELETE CASCADE
);

-- ============================================
-- 14. 코치 관심 분야 (다대다 매핑)
-- ============================================
CREATE TABLE COACH_INTEREST (
    COACH_IDX       NUMBER NOT NULL,                  -- 코치 인덱스 (FK)
    MINOR_FIELD_IDX NUMBER NOT NULL,                  -- 분야 소분류 인덱스 (FK)
    CONSTRAINT PK_COACH_INTEREST PRIMARY KEY (COACH_IDX, MINOR_FIELD_IDX),
    CONSTRAINT FK_CINT_COACH FOREIGN KEY (COACH_IDX) 
        REFERENCES COACH(COACH_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CINT_MINORF FOREIGN KEY (MINOR_FIELD_IDX) 
        REFERENCES MINOR_FIELD(MINOR_FIELD_IDX) ON DELETE CASCADE
);

-- ============================================
-- 15. 컴퍼니 제공 분야 (다대다 매핑)
-- ============================================
CREATE TABLE COMPANY_PROVIDE (
    COMPANY_IDX     NUMBER NOT NULL,                  -- 컴퍼니 인덱스 (FK)
    MINOR_FIELD_IDX NUMBER NOT NULL,                  -- 분야 소분류 인덱스 (FK)
    CONSTRAINT PK_COMPANY_PROVIDE PRIMARY KEY (COMPANY_IDX, MINOR_FIELD_IDX),
    CONSTRAINT FK_COMPROV_COMPANY FOREIGN KEY (COMPANY_IDX) 
        REFERENCES COMPANY(COMPANY_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_COMPROV_MINORF FOREIGN KEY (MINOR_FIELD_IDX) 
        REFERENCES MINOR_FIELD(MINOR_FIELD_IDX) ON DELETE CASCADE
);

-- ============================================
-- 16. 코치 지역(소재지) 매핑
-- ============================================
CREATE TABLE COACH_REGION (
    COACH_IDX        NUMBER       NOT NULL,           -- 코치 인덱스 (FK)
    MAJOR_REGION_IDX NUMBER       NOT NULL,           -- 지역 대분류 인덱스 (FK)
    MINOR_REGION_IDX NUMBER       NOT NULL,           -- 지역 소분류 인덱스 (FK)
    REGION_DETAIL    VARCHAR2(200),                   -- 상세 주소
    CONSTRAINT PK_COACH_REGION PRIMARY KEY (COACH_IDX, MAJOR_REGION_IDX, MINOR_REGION_IDX),
    CONSTRAINT FK_CREG_COACH FOREIGN KEY (COACH_IDX) 
        REFERENCES COACH(COACH_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CREG_MAJORR FOREIGN KEY (MAJOR_REGION_IDX) 
        REFERENCES MAJOR_REGION(MAJOR_REGION_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CREG_MINORR FOREIGN KEY (MINOR_REGION_IDX) 
        REFERENCES MINOR_REGION(MINOR_REGION_IDX) ON DELETE CASCADE
);

-- ============================================
-- 17. 컴퍼니 지역(소재지) 매핑(지점)
-- ============================================
CREATE TABLE COMPANY_REGION (
    COMPANY_IDX      NUMBER       NOT NULL,           -- 컴퍼니 인덱스 (FK)
    MAJOR_REGION_IDX NUMBER       NOT NULL,           -- 지역 대분류 인덱스 (FK)
    MINOR_REGION_IDX NUMBER       NOT NULL,           -- 지역 소분류 인덱스 (FK)
    REGION_DETAIL    VARCHAR2(200),                   -- 상세 주소
    BRANCH_NAME  VARCHAR2(100),   			-- 지점명
    BRANCH_TEL    VARCHAR2(100),                       -- 지점 전화번호
    CONSTRAINT PK_COMPANY_REGION PRIMARY KEY (COMPANY_IDX, MAJOR_REGION_IDX, MINOR_REGION_IDX),
    CONSTRAINT FK_COMPREG_COMPANY FOREIGN KEY (COMPANY_IDX) 
        REFERENCES COMPANY(COMPANY_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_COMPREG_MAJORR FOREIGN KEY (MAJOR_REGION_IDX) 
        REFERENCES MAJOR_REGION(MAJOR_REGION_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_COMPREG_MINORR FOREIGN KEY (MINOR_REGION_IDX) 
        REFERENCES MINOR_REGION(MINOR_REGION_IDX) ON DELETE CASCADE
);

-- ============================================
-- 18. 클래스
-- ============================================
CREATE TABLE CLASS (
    CLASS_IDX        NUMBER        PRIMARY KEY,       -- 클래스 인덱스 (PK)
    PROVIDER_IDX     NUMBER        NOT NULL,          -- 제공자 유저 인덱스 (FK)
    TITLE            VARCHAR2(200) NOT NULL,          -- 클래스 제목
    INTRO            VARCHAR2(3000) NOT NULL,         -- 클래스 소개
    MINOR_FIELD_IDX  NUMBER,                          -- 클래스 분야 소분류 인덱스 (FK)
    MINOR_REGION_IDX NUMBER NOT NULL,                 -- 클래스 장소 소분류 인덱스 (FK)
    REGION_DETAIL    VARCHAR2(200),                   -- 상세 주소
    PRICE            NUMBER,                          -- 가격 (원 단위)
    PHOTO VARCHAR2(100),  			-- 클래스 소개 사진
    VIDEO VARCHAR2(100),  				-- 클래스 소개 영상
    START_DATE       DATE         NOT NULL,           -- 시작 일시
    END_DATE         DATE,                            -- 종료 일시
    CREATED_AT       DATE,                            -- 등록일
    MAX_USER_CNT     NUMBER,                          -- 수강 인원 제한
    CONSTRAINT FK_CLASS_USER FOREIGN KEY (PROVIDER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CLASS_MINORF FOREIGN KEY (MINOR_FIELD_IDX) 
        REFERENCES MINOR_FIELD(MINOR_FIELD_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CLASS_MINORR FOREIGN KEY (MINOR_REGION_IDX) 
        REFERENCES MINOR_REGION(MINOR_REGION_IDX) ON DELETE CASCADE
);

-- ============================================
-- 19. 광고
-- ============================================
CREATE TABLE AD (
    AD_IDX         NUMBER       PRIMARY KEY,          -- 광고 인덱스 (PK)
    MEMBER_IDX     NUMBER       NOT NULL,             -- 멤버십 매핑 인덱스 (FK)
    PHOTO          VARCHAR2(500),                     -- 배너 사진 경로
    CREATED_AT     DATE,                              -- 등록일
    CONSTRAINT FK_AD_MEMBER FOREIGN KEY (MEMBER_IDX) 
        REFERENCES MEMBERSHIP(MEMBER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 20. 서약서(약속)
-- ============================================
CREATE TABLE AGREEMENT (
    AGREEMENT_IDX  NUMBER        PRIMARY KEY,         -- 서약서 인덱스 (PK)
    WRITER_IDX     NUMBER        NOT NULL,            -- 작성자 유저 인덱스 (FK)
    RECEIVER_IDX   NUMBER        NOT NULL,            -- 수신자 유저 인덱스 (FK)
    CONTENT        VARCHAR2(3000) NOT NULL,           -- 교환 내용 상세
    STATUS         VARCHAR2(20),                      -- 서약서 상태
    CREATED_AT     DATE,                              -- 작성 시간
    CONSTRAINT FK_AGREE_WRITER FOREIGN KEY (WRITER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_AGREE_RECEIVER FOREIGN KEY (RECEIVER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 21. 일정 테이블
-- ============================================
CREATE TABLE SCHEDULE (
    SCHEDULE_IDX     NUMBER       PRIMARY KEY,        -- 일정 인덱스 (PK)
    AGREEMENT_IDX    NUMBER       NOT NULL,           -- 서약서 인덱스 (FK)
    START_DATE       DATE         NOT NULL,           -- 시작 일시
    END_DATE         DATE         NOT NULL,              -- 종료 일시
    LOCATION         VARCHAR2(500),                   -- 장소 상세 주소
    CREATED_AT       DATE,                            -- 생성일
    NAME    VARCHAR2(200), -- 일정 제목
    CONTENT VARCHAR2(2000), -- 일정 상세 내용
    CONSTRAINT FK_SCHED_AGREE FOREIGN KEY (AGREEMENT_IDX) 
        REFERENCES AGREEMENT(AGREEMENT_IDX) ON DELETE CASCADE
);

-- ============================================
-- 22. 후기용 해시태그
-- ============================================
CREATE TABLE REVIEW_TAG (
    REVIEW_TAG_IDX NUMBER       PRIMARY KEY,          -- 리뷰 태그 인덱스 (PK)
    REVIEW_TYPE    VARCHAR2(10),                      -- 후기 타입 (CLASS/COACH)
    CONTENT        VARCHAR2(50)                       -- 태그 내용
);

-- ============================================
-- 23. 평점/리뷰 (코치/회원에 대한)
-- ============================================
CREATE TABLE REVIEW_COACH (
    REVIEW_COACH_IDX NUMBER       PRIMARY KEY,        -- 리뷰 인덱스 (PK)
    REVIEWER_IDX     NUMBER       NOT NULL,           -- 리뷰 작성자 유저 인덱스 (FK)
    REVIEWEE_IDX     NUMBER       NOT NULL,           -- 리뷰 대상자 유저 인덱스 (FK)
    RATING           NUMBER(2,1)  NOT NULL,           -- 평점 (0.0 ~ 5.0)
    TAG              VARCHAR2(3000),                   -- 리뷰 태그
    CONTENT          VARCHAR2(3000),                  -- 리뷰 내용
    CREATED_AT       DATE,                            -- 작성일
    CONSTRAINT FK_REVCOACH_REVIEWER FOREIGN KEY (REVIEWER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_REVCOACH_REVIEWEE FOREIGN KEY (REVIEWEE_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 24. 평점/리뷰 (클래스에 대한)
-- ============================================
CREATE TABLE REVIEW_CLASS (
    REVIEW_CLASS_IDX NUMBER       PRIMARY KEY,        -- 리뷰 인덱스 (PK)
    REVIEWER_IDX     NUMBER       NOT NULL,           -- 리뷰 작성자 유저 인덱스 (FK)
    CLASS_IDX        NUMBER       NOT NULL,           -- 리뷰 대상 클래스 인덱스 (FK)
    RATING           NUMBER(2,1)  NOT NULL,           -- 평점 (0.0 ~ 5.0)
    TAG              VARCHAR2(3000),                   -- 리뷰 태그
    CONTENT          VARCHAR2(3000),                  -- 리뷰 내용
    CREATED_AT       DATE,                            -- 작성일
    CONSTRAINT FK_REVCLASS_REVIEWER FOREIGN KEY (REVIEWER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_REVCLASS_CLASS FOREIGN KEY (CLASS_IDX) 
        REFERENCES CLASS(CLASS_IDX) ON DELETE CASCADE
);

-- ============================================
-- 25. 클래스 수강 신청
-- ============================================
CREATE TABLE CLASS_ENROLLMENT (
    ENROLL_IDX   NUMBER       PRIMARY KEY,            -- 수강 신청 인덱스 (PK)
    CLASS_IDX    NUMBER       NOT NULL,               -- 클래스 인덱스 (FK)
    USER_IDX    NUMBER       NOT NULL,               -- 수강생 유저 인덱스 (FK)
    STATUS       VARCHAR2(20),                        -- 수강 상태
    ENROLLED_AT  DATE,                                -- 수강 신청일
    COMPLETED_AT DATE,                                -- 수강 완료일 및 취소일
    CONSTRAINT FK_ENROLL_CLASS FOREIGN KEY (CLASS_IDX) 
        REFERENCES CLASS(CLASS_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_ENROLL_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 26. 토큰 패키지 테이블
-- ============================================
CREATE TABLE TOKEN (
    TOKEN_IDX NUMBER       PRIMARY KEY,               -- 토큰 팩 인덱스 (PK)
    PKG_NAME  VARCHAR2(50),                           -- 패키지 이름
    TOKEN_NUM NUMBER       NOT NULL,                  -- 패키지당 토큰 수
    PRICE     NUMBER       NOT NULL                   -- 패키지 가격 (원 단위)
);

-- ============================================
-- 27. 토큰 사용 내역
-- ============================================
CREATE TABLE TOKEN_HISTORY (
    TOKEN_HIST_IDX NUMBER       PRIMARY KEY,          -- 거래 내역 인덱스 (PK)
    USER_IDX      NUMBER       NOT NULL,             -- 유저 인덱스 (FK)
    HIST_TYPE      VARCHAR2(20) NOT NULL,             -- 거래 타입 (CHAT/MATCH/GIFT/PAY)
    AMOUNT         NUMBER       NOT NULL,             -- 변동 수량
    BALANCE_AFTER  NUMBER       NOT NULL,             -- 거래 후 토큰 수
    CREATED_AT     DATE,                              -- 거래 일시
    CONSTRAINT FK_THIST_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 28. 채팅방
-- ============================================
CREATE TABLE CHAT_ROOM (
    CHAT_ROOM_IDX   NUMBER       PRIMARY KEY,         -- 채팅방 인덱스 (PK)
    USER1_IDX       NUMBER       NOT NULL,            -- 참여자1 유저 인덱스 (FK)
    USER2_IDX       NUMBER       NOT NULL,            -- 참여자2 유저 인덱스 (FK)
    LAST_MESSAGE    VARCHAR2(500),                    -- 마지막 메시지 미리보기
    LAST_MESSAGE_AT DATE,                             -- 마지막 메시지 시간
    LEFT1_YN CHAR(1) DEFAULT 'N' NOT NULL,   	-- 유저 1 채팅방 퇴장 여부
    LEFT2_YN CHAR(1) DEFAULT 'N' NOT NULL,         -- 유저 2 채팅방 퇴장 여부
    UNREAD_COUNT1   NUMBER       DEFAULT 0,           -- 사용자1 읽지 않은 수
    UNREAD_COUNT2   NUMBER       DEFAULT 0,           -- 사용자2 읽지 않은 수
    TEXT_FILE       VARCHAR2(500),                    -- 채팅 텍스트 파일 경로

    CREATED_AT      DATE,                             -- 채팅방 생성일
    CONSTRAINT FK_CHAT_USER1 FOREIGN KEY (USER1_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT UK_CHAT_ROOM UNIQUE (USER1_IDX, USER2_IDX)
);

-- ============================================
-- 29. 알림
-- ============================================
CREATE TABLE NOTIFICATION (
    NOTI_IDX     NUMBER        PRIMARY KEY,           -- 알림 인덱스 (PK)
    RECEIVER_IDX NUMBER        NOT NULL,              -- 수신자 유저 인덱스 (FK)
    PROVIDER_IDX NUMBER        NOT NULL,              -- 발신자 유저 인덱스 (FK)
    NOTI_TYPE    VARCHAR2(50)  NOT NULL,              -- 알림 타입
    CONTENT      VARCHAR2(500) NOT NULL,              -- 알림 내용
    IS_READ      CHAR(1),                             -- 읽음 여부 (Y/N)
    CREATED_AT   DATE,                                -- 생성 시간
    CONSTRAINT FK_NOTI_RECEIVER FOREIGN KEY (RECEIVER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_NOTI_PROVIDER FOREIGN KEY (PROVIDER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 30. 결제 내역
-- ============================================
CREATE TABLE PAY (
    PAY_IDX          NUMBER       PRIMARY KEY,       -- 결제 내역 인덱스 (PK)
    TID              VARCHAR2(255),
    CID              VARCHAR2(255),
    SID              VARCHAR2(255),
    PARTNER_ORDER_ID VARCHAR2(255),
    PARTNER_USER_ID  VARCHAR2(255),
    ITEM_NAME        VARCHAR2(500),
    ITEM_CODE        VARCHAR2(255),
    QUANTITY         NUMBER,
    CREATED_AT       DATE,
    APPROVED_AT      DATE,
    TOTAL            NUMBER,
    TAX_FREE         NUMBER,
    VAT              NUMBER,
    POINT            NUMBER,
    DISCOUNT         NUMBER,
    GREEN_DEPOSIT    NUMBER,
    PAY_TYPE         VARCHAR2(50)       -- 결제 타입(CLASS, TOKEN, MEMBERSHIP)
);

-- ============================================
-- 31. 사유 타입 (자주 쓰는 질문이나 약관)
-- ============================================
CREATE TABLE REASON_TYPE (
    REASON_TYPE_IDX NUMBER        PRIMARY KEY,        -- 사유 타입 인덱스 (PK)
    TYPE_NAME       VARCHAR2(50),                     -- 사유 타입명
    CONTENT         VARCHAR2(3000)                    -- 내용
);

-- ============================================
-- 32. 신고
-- ============================================
CREATE TABLE REPORT (
    REPORT_IDX      NUMBER        PRIMARY KEY,        -- 신고 인덱스 (PK)
    REPORTER_IDX    NUMBER        NOT NULL,           -- 신고자 유저 인덱스 (FK)
    REPORTED_IDX    NUMBER        NOT NULL,           -- 피신고 대상 유저 인덱스 (FK)
    REASON_TYPE_IDX NUMBER,                           -- 신고 사유 유형 인덱스 (FK)
    CONTENT         VARCHAR2(3000) NOT NULL,          -- 신고 내용 상세
    STATUS          VARCHAR2(20),                     -- 처리 상태
    CREATED_AT      DATE,                             -- 신고 접수일
    UPDATED_AT      DATE,                             -- 처리 상태 변경일
    CONSTRAINT FK_REPORT_REPORTER FOREIGN KEY (REPORTER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_REPORT_REPORTED FOREIGN KEY (REPORTED_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_REPORT_REASON FOREIGN KEY (REASON_TYPE_IDX) 
        REFERENCES REASON_TYPE(REASON_TYPE_IDX) ON DELETE CASCADE
);

-- ============================================
-- 33. 좋아요 (회원)
-- ============================================
CREATE TABLE LIKES_USER (
    LIKES_USER_IDX NUMBER       PRIMARY KEY,          -- 좋아요 인덱스 (PK)
    LIKER_IDX      NUMBER       NOT NULL,             -- 좋아요 누른 유저 인덱스 (FK)
    LIKED_USER_IDX NUMBER       NOT NULL,             -- 좋아요 대상 유저 인덱스 (FK)
    CREATED_AT     DATE,                              -- 좋아요 누른 시간
    CONSTRAINT FK_LIKEU_LIKER FOREIGN KEY (LIKER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_LIKEU_LIKED FOREIGN KEY (LIKED_USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT UK_LIKES_USER UNIQUE (LIKER_IDX, LIKED_USER_IDX)
);

-- ============================================
-- 34. 좋아요 (클래스)
-- ============================================
CREATE TABLE LIKES_CLASS (
    LIKES_CLASS_IDX NUMBER       PRIMARY KEY,         -- 좋아요 인덱스 (PK)
    LIKER_IDX       NUMBER       NOT NULL,            -- 좋아요 누른 유저 인덱스 (FK)
    CLASS_IDX       NUMBER       NOT NULL,            -- 좋아요 대상 클래스 인덱스 (FK)
    CREATED_AT      DATE,                             -- 좋아요 누른 시간
    CONSTRAINT FK_LIKEC_LIKER FOREIGN KEY (LIKER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_LIKEC_CLASS FOREIGN KEY (CLASS_IDX) 
        REFERENCES CLASS(CLASS_IDX) ON DELETE CASCADE,
    CONSTRAINT UK_LIKES_CLASS UNIQUE (LIKER_IDX, CLASS_IDX)
);

-- ============================================
-- 35. QNA(문의하기)
-- ============================================
CREATE TABLE QNA (
    QNA_IDX        NUMBER        PRIMARY KEY,         -- QnA 인덱스 (PK)
    USER_IDX NUMBER        NOT NULL,            -- 유저 인덱스 (FK)
    QNA_TYPE       VARCHAR2(20)  NOT NULL,            -- QnA 타입
    TITLE          VARCHAR2(200) NOT NULL,            -- 질문 제목
    QUESTION       VARCHAR2(3000) NOT NULL,           -- 질문 내용
    CREATED_AT     DATE,                              -- 등록일
    CONSTRAINT FK_QNA_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 36. QNA_A(문의답변)
-- ============================================
CREATE TABLE QNA_A (
    QNA_A_IDX        NUMBER        PRIMARY KEY,         -- QnA 답변 인덱스 (PK)
    QNA_IDX NUMBER        NOT NULL,            -- QNA 인덱스 (FK)
    TITLE          VARCHAR2(200) NOT NULL,            -- 답변 제목
    ANSWER       VARCHAR2(3000) NOT NULL,           -- 답변 내용
    CREATED_AT     DATE,                              -- 등록일
    CONSTRAINT FK_QNA_A_QNA FOREIGN KEY (QNA_IDX) 
        REFERENCES QNA(QNA_IDX) ON DELETE CASCADE
);

-- ============================================
-- 37. 사유 로그 (탈퇴나 부정 선택지에 관한 기록 관리)
-- ============================================
CREATE TABLE REASON_LOG (
    REASON_LOG_IDX  NUMBER       PRIMARY KEY,         -- 사유 로그 인덱스 (PK)
    USER_TYPE       VARCHAR2(30),           			 -- 사유 작성 대상 유저 타입
    REASON_TYPE_IDX NUMBER       NOT NULL,            -- 사유 타입 인덱스 (FK)
    CREATED_AT      DATE,                             -- 작성 시간
    CONSTRAINT FK_RLOG_REASON FOREIGN KEY (REASON_TYPE_IDX) 
        REFERENCES REASON_TYPE(REASON_TYPE_IDX) ON DELETE CASCADE
);

-- ============================================
-- 38. 랭크
-- ============================================
CREATE TABLE RANK (
    RANK_IDX  NUMBER PRIMARY KEY,                     -- 랭크 인덱스(PK)
    RANK_NAME VARCHAR2(100) NOT NULL UNIQUE,          -- 랭크 이름
    MIN_SCORE NUMBER,                                 -- 랭크 최소점수
    MAX_SCORE NUMBER                                  -- 랭크 최고 점수
);

-- ============================================
-- 39. 자격증/등록증
-- ============================================
CREATE TABLE CERT(
    CERT_IDX  NUMBER PRIMARY KEY,                     -- 자격증 인덱스(PK)
    USER_IDX NUMBER NOT NULL, 				-- 유저 인덱스(FK)
    CERT_NAME VARCHAR2(50),         		 -- 자격증/등록증 이름
    GET_DATE DATE, 							-- 발급일자
    CERT_FROM VARCHAR2(50),					-- 발급 기관
    CERT_TYPE VARCHAR2(100), 					-- 종류(자격증/사업자등록증)
    CERT_FILE VARCHAR2(500), 					-- 파일 경로
    CERT_STATUS VARCHAR2(10), 				        -- 승인 상태(대기/확인)
    CONSTRAINT FK_CERT_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 40. 검열 키워드 
-- ============================================
CREATE TABLE KEYWORD(
    KEYWORD_IDX  NUMBER PRIMARY KEY,                     -- 검열 키워드 인덱스(PK)
    KEYWORD_TYPE VARCHAR2(50), 				-- 검열 단어 타입(성인/정치/욕설)
    KEYWORD_NAME VARCHAR2(50)          			-- 키워드 단어
);

-- ============================================
-- 41. 유저 - 해시태그 매핑 
-- ============================================
CREATE TABLE USER_HASHTAG (
    USER_IDX    NUMBER NOT NULL,
    HASHTAG_IDX NUMBER NOT NULL,
    CONSTRAINT PK_USER_HASHTAG PRIMARY KEY (USER_IDX, HASHTAG_IDX),
    CONSTRAINT FK_UHTAG_USER FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_UHTAG_TAG FOREIGN KEY (HASHTAG_IDX) 
        REFERENCES HASHTAG(HASHTAG_IDX) ON DELETE CASCADE
);

-- ============================================
-- 42. 유저 - 해시태그 매핑 2
-- ============================================
CREATE TABLE USER_HASHTAG_LIKE (
    USER_IDX    NUMBER NOT NULL,
    HASHTAG_IDX NUMBER NOT NULL,
    CONSTRAINT PK_USER_HASHTAG_LIKE PRIMARY KEY (USER_IDX, HASHTAG_IDX),
    CONSTRAINT FK_UHTAG_USER_LIKE FOREIGN KEY (USER_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_UHTAG_TAG_LIKE FOREIGN KEY (HASHTAG_IDX) 
        REFERENCES HASHTAG(HASHTAG_IDX) ON DELETE CASCADE
);

-- ============================================
-- 43. 클래스 - 해시태그 매핑 
-- ============================================
CREATE TABLE CLASS_HASHTAG (
    CLASS_IDX   NUMBER NOT NULL,
    HASHTAG_IDX NUMBER NOT NULL,
    CONSTRAINT PK_CLASS_HASHTAG PRIMARY KEY (CLASS_IDX, HASHTAG_IDX),
    CONSTRAINT FK_CLTAG_CLASS FOREIGN KEY (CLASS_IDX) 
        REFERENCES CLASS(CLASS_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_CLTAG_TAG FOREIGN KEY (HASHTAG_IDX) 
        REFERENCES HASHTAG(HASHTAG_IDX) ON DELETE CASCADE
);

-- ============================================
-- 44. 매칭 
-- ============================================
CREATE TABLE MATCH (
    MATCH_IDX   NUMBER       PRIMARY KEY,         -- 매칭 인덱스 (PK)
    USER1_IDX       NUMBER       NOT NULL,            -- 유저1 인덱스 (FK)
    USER2_IDX       NUMBER       NOT NULL,            -- 유저2 인덱스 (FK)
    CHAT_APPLICANT	  NUMBER, 				-- 채팅신청자 인덱스
    IS_CHAT      CHAR(1),                             		-- 채팅 수락 여부 (Y/N)
    MATCH_APPLICANT NUMBER, 					-- 매칭신청자 인덱스
    IS_MATCH      CHAR(1),                             	-- 매칭 수락 여부 (Y/N)
    CONSTRAINT FK_MATCH_USER1 FOREIGN KEY (USER1_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE,
    CONSTRAINT FK_MATCH_USER2 FOREIGN KEY (USER2_IDX) 
        REFERENCES USERS(USER_IDX) ON DELETE CASCADE
);

-- ============================================
-- 45. 결제 상태
-- ============================================
CREATE TABLE PAY_STATUS (
    TID              VARCHAR2(255),
    PARTNER_ORDER_ID VARCHAR2(255),
    PARTNER_USER_ID  VARCHAR2(255),
    STATUS   VARCHAR2(50),
    PAYLOAD             VARCHAR2(3000)
);

-- ============================================
-- 46. 도전과제 테이블
-- ============================================
CREATE TABLE CHALLENGE (
    CHALLENGE_IDX NUMBER PRIMARY KEY,        -- 도전과제 인덱스 (오타 수정)
    NAME VARCHAR2(500) NOT NULL,             -- 도전과제 이름
    TYPE VARCHAR2(100) NOT NULL,             -- 종류(POINT, LIKE, MATCH, REVIEW)
    QUANTITY NUMBER NOT NULL,                -- 목표 수치
    PRIZE NUMBER DEFAULT 0                   -- 달성 시 지급할 포인트
);

-- ============================================
-- 47. 유저-도전과제 매핑 테이블
-- ============================================
CREATE TABLE USER_CHALLENGE (
    USER_IDX NUMBER NOT NULL,                -- 유저 FK
    CHALLENGE_IDX NUMBER NOT NULL,           -- 도전과제 FK
    ACHIEVE CHAR(1) DEFAULT 'N',             -- 달성여부(Y/N)
    ACHIEVED_DATE DATE,                      -- 달성 일시
    CONSTRAINT PK_USER_CHALLENGE PRIMARY KEY (USER_IDX, CHALLENGE_IDX),
    CONSTRAINT FK_UC_USER FOREIGN KEY (USER_IDX) REFERENCES USERS(USER_IDX), -- 유저 테이블 참조
    CONSTRAINT FK_UC_CHALLENGE FOREIGN KEY (CHALLENGE_IDX) REFERENCES CHALLENGE(CHALLENGE_IDX)
);

-- ============================================
-- 48. 환불
-- ============================================
CREATE TABLE PAY_CANCEL (
    TID              VARCHAR2(255),
    PARTNER_ORDER_ID VARCHAR2(255),
    PARTNER_USER_ID  VARCHAR2(255),
    CANCELED_AT       DATE,
    PAYLOAD             VARCHAR2(3000),
    TOTAL            NUMBER,
    TAX_FREE         NUMBER,
    VAT              NUMBER,  			-- 결제 내역에는 TAX로 되어있음
    POINT            NUMBER,
    DISCOUNT         NUMBER,
    GREEN_DEPOSIT    NUMBER
);


COMMIT;

```
</details>

---
<details>
  
<summary>📄 DB 샘플 데이터</summary>

```
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '서울특별시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '부산광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '대구광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '인천광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '광주광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '대전광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '울산광역시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '세종특별자치시');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '경기도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '강원특별자치도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '충청북도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '충청남도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '전북특별자치도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '전라남도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '경상북도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '경상남도');
INSERT INTO MAJOR_REGION (MAJOR_REGION_IDX, REGION_NM) VALUES (SEQ_MAJOR_REGION.NEXTVAL, '제주특별자치도');

-- 1. 서울특별시 (25개 구 전체)
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '강남구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '강동구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '강북구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '강서구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '관악구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '광진구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '구로구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '금천구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '노원구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '도봉구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '동대문구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '동작구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '마포구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '서대문구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '서초구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '성동구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '성북구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '송파구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '양천구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '영등포구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '용산구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '은평구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '종로구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '중구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '중랑구' FROM MAJOR_REGION WHERE REGION_NM = '서울특별시';

-- 2. 경기도 
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '수원시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '성남시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '고양시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '용인시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '부천시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '안산시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '안양시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '남양주시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '화성시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '평택시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '의정부시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '시흥시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '파주시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '광명시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '김포시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '군포시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '광주시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '이천시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '양주시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '오산시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '구리시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '안성시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '포천시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '의왕시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '하남시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '여주시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '동두천시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '과천시' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '양평군' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '가평군' FROM MAJOR_REGION WHERE REGION_NM = '경기도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '연천군' FROM MAJOR_REGION WHERE REGION_NM = '경기도';


-- 3. 기타 광역시 및 주요 지역
-- 이미 주신 샘플
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '해운대구' FROM MAJOR_REGION WHERE REGION_NM = '부산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '수성구' FROM MAJOR_REGION WHERE REGION_NM = '대구광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '연수구' FROM MAJOR_REGION WHERE REGION_NM = '인천광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '유성구' FROM MAJOR_REGION WHERE REGION_NM = '대전광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '남구' FROM MAJOR_REGION WHERE REGION_NM = '광주광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '조치원읍' FROM MAJOR_REGION WHERE REGION_NM = '세종특별자치시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '춘천시' FROM MAJOR_REGION WHERE REGION_NM = '강원특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '청주시' FROM MAJOR_REGION WHERE REGION_NM = '충청북도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '천안시' FROM MAJOR_REGION WHERE REGION_NM = '충청남도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '전주시' FROM MAJOR_REGION WHERE REGION_NM = '전북특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '여수시' FROM MAJOR_REGION WHERE REGION_NM = '전라남도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '포항시' FROM MAJOR_REGION WHERE REGION_NM = '경상북도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '창원시' FROM MAJOR_REGION WHERE REGION_NM = '경상남도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '제주시' FROM MAJOR_REGION WHERE REGION_NM = '제주특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM) 
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '서귀포시' FROM MAJOR_REGION WHERE REGION_NM = '제주특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '중구'   FROM MAJOR_REGION WHERE REGION_NM = '부산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '부산진구' FROM MAJOR_REGION WHERE REGION_NM = '부산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '사하구' FROM MAJOR_REGION WHERE REGION_NM = '부산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '달서구' FROM MAJOR_REGION WHERE REGION_NM = '대구광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '달성군' FROM MAJOR_REGION WHERE REGION_NM = '대구광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '남동구' FROM MAJOR_REGION WHERE REGION_NM = '인천광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '부평구' FROM MAJOR_REGION WHERE REGION_NM = '인천광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '서구' FROM MAJOR_REGION WHERE REGION_NM = '대전광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '광산구' FROM MAJOR_REGION WHERE REGION_NM = '광주광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '중구' FROM MAJOR_REGION WHERE REGION_NM = '울산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '울주군' FROM MAJOR_REGION WHERE REGION_NM = '울산광역시';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '강릉시' FROM MAJOR_REGION WHERE REGION_NM = '강원특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '원주시' FROM MAJOR_REGION WHERE REGION_NM = '강원특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '충주시' FROM MAJOR_REGION WHERE REGION_NM = '충청북도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '아산시' FROM MAJOR_REGION WHERE REGION_NM = '충청남도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '익산시' FROM MAJOR_REGION WHERE REGION_NM = '전북특별자치도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '목포시' FROM MAJOR_REGION WHERE REGION_NM = '전라남도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '경주시' FROM MAJOR_REGION WHERE REGION_NM = '경상북도';
INSERT INTO MINOR_REGION (MINOR_REGION_IDX, MAJOR_REGION_IDX, DISTRICT_NM)
SELECT SEQ_MINOR_REGION.NEXTVAL, MAJOR_REGION_IDX, '진주시' FROM MAJOR_REGION WHERE REGION_NM = '경상남도';


-----------------------------------------------------------
-- [1. IT/전자]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, 'IT/전자');
-- 소분류 10개
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '자바');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '파이썬');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '웹개발');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '앱개발');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '클라우드');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '데이터베이스');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '정보보안');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '인공지능');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '게임개발');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '임베디드');

-----------------------------------------------------------
-- [2. 예술]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '예술');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '보컬');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '피아노');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '작곡');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '수채화');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '사진');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '연기');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '댄스');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '도예');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '일러스트');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '기타');

-----------------------------------------------------------
-- [3. 어학]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '어학');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '영어회화');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '토익');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '일본어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '중국어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '프랑스어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '독일어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '스페인어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '베트남어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '한국어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '이탈리아어');

-----------------------------------------------------------
-- [4. 투자/금융]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '투자/금융');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '주식');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '부동산');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '비트코인');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '재테크');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '세무');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '보험');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '창업');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '회계');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '연금');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '경매');

-----------------------------------------------------------
-- [5. 스포츠]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '스포츠');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '축구');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '농구');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '골프');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '수영');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '테니스');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '필라테스');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '요가');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '웨이트');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '클라이밍');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '서핑');

-----------------------------------------------------------
-- [6. 생활]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '생활');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '정리수납');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '반려견훈련');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '운전연수');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '심리상담');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '꽃꽂이');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '목공');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '법률상담');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '명상');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '세탁');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '부모교육');

-----------------------------------------------------------
-- [7. 패션/뷰티]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '패션/뷰티');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '메이크업');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '헤어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '퍼스널컬러');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '네일아트');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '스타일링');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '피부관리');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '향수');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '왁싱');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '타투');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '모델링');

-----------------------------------------------------------
-- [8. 인테리어]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '인테리어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '셀프인테리어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '조명디자인');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '가구배치');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '공간컨설팅');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '식물인테리어');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '소품코디');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '주방리폼');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '욕실리폼');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '수납공간');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '3D설계');

-----------------------------------------------------------
-- [9. 요리]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '요리');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '한식');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '양식');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '일식');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '중식');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '베이킹');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '바리스타');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '와인');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '칵테일');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '비건요리');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '자취요리');

-----------------------------------------------------------
-- [10. 게임]
-----------------------------------------------------------
INSERT INTO MAJOR_FIELD VALUES (SEQ_MAJOR_FIELD.NEXTVAL, '게임');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '롤(LoL)');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '배틀그라운드');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '오버워치');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '발로란트');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '체스');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '바둑');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '보드게임');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '철권');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '피파');
INSERT INTO MINOR_FIELD VALUES (SEQ_MINOR_FIELD.NEXTVAL, SEQ_MAJOR_FIELD.CURRVAL, '스타크래프트');

-- ============================================
-- HASHTAG 샘플 데이터 (50개)
-- ============================================

-- 성향/특성 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#친절함');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#열정적');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#꼼꼼함');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#유쾌함');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#차분함');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#체계적');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#섬세함');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#피드백꼼꼼');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#경력풍부');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#전문가');

-- 수강생 대상 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#초보환영');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#중급이상');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#고급자환영');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#입문자추천');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#키즈클래스');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#성인전용');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#시니어환영');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#20대추천');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#30대추천');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#직장인추천');

-- 수업 방식 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#1대1수업');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#그룹수업');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#소수정예');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#맞춤형수업');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#온라인가능');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#오프라인');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#방문레슨');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#단기집중');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#장기코스');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#무료체험');

-- 시간대 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#평일오전');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#평일오후');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#평일저녁');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#주말반');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#새벽반');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#심야가능');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#시간협의가능');

-- 목적/효과 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#다이어트');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#체력증진');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#근력강화');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#유연성향상');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#스트레스해소');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#취미생활');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#자격증대비');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#대회준비');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#재활운동');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#바른자세');

-- 가격/혜택 관련
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#가성비최고');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#합리적가격');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#할인이벤트');
INSERT INTO HASHTAG VALUES (SEQ_HASHTAG.NEXTVAL, '#패키지할인');

INSERT INTO RANK (RANK_IDX, RANK_NAME, MIN_SCORE, MAX_SCORE)
VALUES (SEQ_RANK.NEXTVAL, '일반 코치', 0, 200);

-- 베테랑 코치 (200 ~ 500)
INSERT INTO RANK (RANK_IDX, RANK_NAME, MIN_SCORE, MAX_SCORE)
VALUES (SEQ_RANK.NEXTVAL, '베테랑 코치', 200, 500);

-- 수석 코치 (500 ~ 1000)
INSERT INTO RANK (RANK_IDX, RANK_NAME, MIN_SCORE, MAX_SCORE)
VALUES (SEQ_RANK.NEXTVAL, '수석 코치', 500, 1000);

-- 마스터 코치 (1000 이상)
INSERT INTO RANK (RANK_IDX, RANK_NAME, MIN_SCORE, MAX_SCORE)
VALUES (SEQ_RANK.NEXTVAL, '마스터 코치', 1000, NULL);

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '욕설', '바보');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '욕설', '멍청이');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '정치', '대통령');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '성인', '조건만남');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '광고', '카지노');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '기타', '관리자');

INSERT INTO KEYWORD (KEYWORD_IDX, KEYWORD_TYPE, KEYWORD_NAME) 
VALUES (SEQ_KEYWORD.NEXTVAL, '비방', '쓰레기');

insert into membership_detail values(1,'일반',0,'없음',2);

insert into membership_detail values(2,'프리미엄',10000,'있음',5);

INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'COACH', '#친절해요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'COACH', '#답장이 빨라요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'COACH', '#전문적이에요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'COACH', '#설명이 자세해요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'COACH', '#시간 약속을 잘 지켜요');

-- 2. CLASS 관련 태그 (강의 내용이나 구성 관련)
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'CLASS', '#커리큘럼이 알차요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'CLASS', '#핵심만 쏙쏙 알려줘요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'CLASS', '#준비물이 꼼꼼해요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'CLASS', '#초보자도 따라하기 쉬워요');
INSERT INTO REVIEW_TAG (REVIEW_TAG_IDX, REVIEW_TYPE, CONTENT) VALUES (seq_review_tag.nextval, 'CLASS', '#공간이 쾌적해요');

INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '탈퇴', '나에게 필요한 기능이 충분하지 않음');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '탈퇴', '서비스 이용이 불편하거나 오류가 많음');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '탈퇴', '원하는 코치/클래스를 찾기 어려움');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '탈퇴', '서비스 이용 비용이 부담됨');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '탈퇴', '기타 사유');

INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '베테랑 코치', 'POINT', 100, 50);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '수석코치', 'POINT', 200, 100);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '마스터 코치', 'POINT', 500, 250);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '레전드 코치', 'POINT', 1000, 500);

INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '인기 예감', 'LIKE', 1, 10);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '동네 스타', 'LIKE', 50, 50);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '대세 코치', 'LIKE', 100, 150);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '팬클럽 결성', 'LIKE', 500, 500);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '모두의 우상', 'LIKE', 1000, 1000);

INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '첫 만남의 설렘', 'MATCH', 1, 20);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '매칭의 맛', 'MATCH', 10, 50);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '프로 매칭러', 'MATCH', 50, 200);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '베테랑 코칭', 'MATCH', 100, 500);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '전설의 마스터', 'MATCH', 500, 1000);

INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '첫 리뷰의 추억', 'REVIEW', 1, 10);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '성실한 리뷰어', 'REVIEW', 5, 30);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '리뷰 대장', 'REVIEW', 20, 100);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '영향력 있는 비평가', 'REVIEW', 50, 300);
INSERT INTO challenge (challenge_idx, name, type, quantity, prize) VALUES (seq_challenge.nextval, '리뷰의 신', 'REVIEW', 100, 700);

INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '신고', '욕설/비방');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '신고', '사기/허위 정보');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '신고', '노쇼/약속 불이행');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '신고', '부적절한 콘텐츠');
INSERT INTO REASON_TYPE VALUES (SEQ_REASON_TYPE.NEXTVAL, '신고', '기타');

INSERT INTO "SCOTT"."REASON_TYPE" (REASON_TYPE_IDX, TYPE_NAME, CONTENT) VALUES (SEQ_REASON_TYPE.NEXTVAL, '멤버십', '가격이 너무 비쌈');
INSERT INTO "SCOTT"."REASON_TYPE" (REASON_TYPE_IDX, TYPE_NAME, CONTENT) VALUES (SEQ_REASON_TYPE.NEXTVAL, '멤버십', '다른 사이트를 이용중');
INSERT INTO "SCOTT"."REASON_TYPE" (REASON_TYPE_IDX, TYPE_NAME, CONTENT) VALUES (SEQ_REASON_TYPE.NEXTVAL', '멤버십', '해택이 너무 적음');
INSERT INTO "SCOTT"."REASON_TYPE" (REASON_TYPE_IDX, TYPE_NAME, CONTENT) VALUES (SEQ_REASON_TYPE.NEXTVAL, '멤버십', '사이트 이용 인원이 너무 적음');
INSERT INTO "SCOTT"."REASON_TYPE" (REASON_TYPE_IDX, TYPE_NAME, CONTENT) VALUES (SEQ_REASON_TYPE.NEXTVAL, '멤버십', '기타');

INSERT INTO users VALUES (SEQ_users.NEXTVAL, 'ADMIN', '관리자', 'admin1', 'admin1', 'ACTIVE', sysdate);


COMMIT;
```
</details>

---

<details>
<summary>📄 DB 테이블 지우기</summary>

```
-- ============================================
-- DROP ALL OBJECTS (역순으로 삭제)
-- ============================================

-- 1. 매핑 테이블 삭제 (외래키 제약 없음)
DROP TABLE USER_CHALLENGE CASCADE CONSTRAINTS;
DROP TABLE USER_HASHTAG CASCADE CONSTRAINTS;
DROP TABLE USER_HASHTAG_LIKE CASCADE CONSTRAINTS;
DROP TABLE CLASS_HASHTAG CASCADE CONSTRAINTS;
DROP TABLE COACH_PROVIDE CASCADE CONSTRAINTS;
DROP TABLE COACH_INTEREST CASCADE CONSTRAINTS;
DROP TABLE COMPANY_PROVIDE CASCADE CONSTRAINTS;
DROP TABLE COACH_REGION CASCADE CONSTRAINTS;
DROP TABLE COMPANY_REGION CASCADE CONSTRAINTS;

-- 2. 종속 테이블 삭제
DROP TABLE CHALLENGE CASCADE CONSTRAINTS;
DROP TABLE MATCH CASCADE CONSTRAINTS;
DROP TABLE LIKES_CLASS CASCADE CONSTRAINTS;
DROP TABLE LIKES_USER CASCADE CONSTRAINTS;
DROP TABLE REPORT CASCADE CONSTRAINTS;
DROP TABLE REASON_LOG CASCADE CONSTRAINTS;
DROP TABLE QNA_A CASCADE CONSTRAINTS;
DROP TABLE QNA CASCADE CONSTRAINTS;
DROP TABLE NOTIFICATION CASCADE CONSTRAINTS;
DROP TABLE CHAT_ROOM CASCADE CONSTRAINTS;
DROP TABLE TOKEN_HISTORY CASCADE CONSTRAINTS;
DROP TABLE CLASS_ENROLLMENT CASCADE CONSTRAINTS;
DROP TABLE REVIEW_CLASS CASCADE CONSTRAINTS;
DROP TABLE REVIEW_COACH CASCADE CONSTRAINTS;
DROP TABLE SCHEDULE CASCADE CONSTRAINTS;
DROP TABLE AGREEMENT CASCADE CONSTRAINTS;
DROP TABLE AD CASCADE CONSTRAINTS;
DROP TABLE CLASS CASCADE CONSTRAINTS;
DROP TABLE CERT CASCADE CONSTRAINTS;
DROP TABLE PAY CASCADE CONSTRAINTS;
DROP TABLE PAY_STATUS CASCADE CONSTRAINTS;
DROP TABLE PAY_CANCEL CASCADE CONSTRAINTS;

-- 3. 멤버십 관련 테이블 삭제
DROP TABLE MEMBERSHIP CASCADE CONSTRAINTS;
DROP TABLE MEMBERSHIP_DETAIL CASCADE CONSTRAINTS;

-- 4. 유저 상세 테이블 삭제
DROP TABLE EMBEDDED_USER CASCADE CONSTRAINTS;
DROP TABLE COMPANY CASCADE CONSTRAINTS;
DROP TABLE COACH CASCADE CONSTRAINTS;

-- 5. 기본 테이블 삭제
DROP TABLE USERS CASCADE CONSTRAINTS;
DROP TABLE REVIEW_TAG CASCADE CONSTRAINTS;
DROP TABLE TOKEN CASCADE CONSTRAINTS;
DROP TABLE KEYWORD CASCADE CONSTRAINTS;
DROP TABLE RANK CASCADE CONSTRAINTS;
DROP TABLE REASON_TYPE CASCADE CONSTRAINTS;
DROP TABLE HASHTAG CASCADE CONSTRAINTS;
DROP TABLE MINOR_REGION CASCADE CONSTRAINTS;
DROP TABLE MAJOR_REGION CASCADE CONSTRAINTS;
DROP TABLE MINOR_FIELD CASCADE CONSTRAINTS;
DROP TABLE MAJOR_FIELD CASCADE CONSTRAINTS;

-- 6. 시퀀스 삭제
DROP SEQUENCE SEQ_CHALLENGE;
DROP SEQUENCE SEQ_MATCH;
DROP SEQUENCE SEQ_KEYWORD;
DROP SEQUENCE SEQ_CERT;
DROP SEQUENCE SEQ_RANK;
DROP SEQUENCE SEQ_EMBED_USER;
DROP SEQUENCE SEQ_REASON_LOG;
DROP SEQUENCE SEQ_REASON_TYPE;
DROP SEQUENCE SEQ_AD;
DROP SEQUENCE SEQ_MEMBERSHIP_DETAIL;
DROP SEQUENCE SEQ_MEMBERSHIP;
DROP SEQUENCE SEQ_QNA_A;
DROP SEQUENCE SEQ_QNA;
DROP SEQUENCE SEQ_LIKES_CLASS;
DROP SEQUENCE SEQ_LIKES_USER;
DROP SEQUENCE SEQ_REPORT;
DROP SEQUENCE SEQ_TOKEN_HISTORY;
DROP SEQUENCE SEQ_PAY;
DROP SEQUENCE SEQ_CLASS_ENROLLMENT;
DROP SEQUENCE SEQ_CLASS;
DROP SEQUENCE SEQ_SCHEDULE;
DROP SEQUENCE SEQ_AGREEMENT;
DROP SEQUENCE SEQ_NOTIFICATION;
DROP SEQUENCE SEQ_CHAT_ROOM;
DROP SEQUENCE SEQ_TOKEN;
DROP SEQUENCE SEQ_REVIEW_TAG;
DROP SEQUENCE SEQ_REVIEW_CLASS;
DROP SEQUENCE SEQ_REVIEW_COACH;
DROP SEQUENCE SEQ_MINOR_REGION;
DROP SEQUENCE SEQ_MAJOR_REGION;
DROP SEQUENCE SEQ_HASHTAG;
DROP SEQUENCE SEQ_MINOR_FIELD;
DROP SEQUENCE SEQ_MAJOR_FIELD;
DROP SEQUENCE SEQ_COMPANY;
DROP SEQUENCE SEQ_COACH;
DROP SEQUENCE SEQ_USERS;

COMMIT;
```

</details>
