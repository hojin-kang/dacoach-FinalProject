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

### 🔑 주요 설정 항목

| 항목 | 설명 |
|------|------|
| `spring.datasource.username` | Oracle DB 사용자 이름 |
| `spring.datasource.password` | Oracle DB 비밀번호 |
| `spring.mail.username` | 발신에 사용할 Gmail 주소 |
| `spring.mail.password` | Gmail **앱 비밀번호** (계정 비밀번호 ❌) |

---

### 📌 Gmail 앱 비밀번호 발급 방법

1. [Google 계정 보안 설정](https://myaccount.google.com/security) 접속
2. **2단계 인증** 활성화
3. **앱 비밀번호** 생성 → 앱: `메일`, 기기: `Windows 컴퓨터` (또는 기타)
4. 생성된 16자리 비밀번호를 `spring.mail.password`에 입력
