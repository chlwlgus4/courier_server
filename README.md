
# 택배 서버 프로젝트 가이드

## 개발 환경 설정

1. 프로필

   각 환경에 맞는 프로필 설정을 위해 `application-{profile}.yml` 파일을 생성해야 합니다.
   ```yaml
   spring:
    datasource:
      url: jdbc:mariadb://localhost:3306/courier
      username: courier
      password: courier123!
      driver-class-name: org.mariadb.jdbc.Driver
      hikari:
        maximum-pool-size: 10     # 최대 커넥션 수
        minimum-idle: 5           # 최소 유휴 커넥션 수
        idle-timeout: 600000      # 10분 커넥션 유휴 타임아웃 시간
        max-lifetime: 1800000     # 30분 커넥션 최대 수명 시간
        connection-timeout: 30000 # 30초 커넥션 획득 타임아웃 시간
        pool-name: CourierHikariCP
      jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
       properties:
         hibernate:
           format_sql: true
           dialect: org.hibernate.dialect.MariaDBDialect
      data:
       redis:
         host: chlwlgus91.synology.me
         port: 5406
   
   cookie:
    secure: false
   
   jwt:
    secret: dummy_secret_for_development_dummy_secret_!
    expiration-ms: 3600000
    refresh-expiration-ms: 604800000
   
   server:
   port: 8080
   ```
   이 설정을 각 환경(local, dev, prod 등)에 맞게 수정하여 사용하세요. 환경변수나 시스템 속성을 통해 값을 주입할 수도 있습니다.
   
   참고: `spring.profiles.active` 속성은 프로필별 구성 파일 내부에서 사용하지 마세요. 이 속성은 기본 application.yml 파일이나 JVM 파라미터, 환경변수를 통해 설정해야 합니다.

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
