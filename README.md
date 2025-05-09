
# 택배 서버 프로젝트 가이드

## 개발 환경 설정

1. 생성된 파일에서 다음 값들을 적절히 변경:
    - JWT_SECRET: JWT 서명에 사용되는 비밀키 (최소 32바이트 길이)
    - JWT_EXPIRATION_MS: 토큰 만료 시간 (밀리초 단위)

2. 애플리케이션 실행 시 프로필 지정:
   ```
   ./gradlew bootRun --args='--spring.profiles.active=local'
   ```

## 프로젝트 구조

본 택배 서버 프로젝트는 Spring Boot 기반으로 구현되었으며, JWT를 이용한 인증 시스템을 사용합니다.

### 주요 기능
- 사용자 인증 및 인가 (JWT 토큰 기반)
- 택배 서비스 관리
- 사용자 관리

## 기술 스택

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT 인증 (io.jsonwebtoken)
- Gradle

## API 문서

API 문서는 Swagger를 통해 제공됩니다. 서버 실행 후 다음 URL에서 확인할 수 있습니다:
