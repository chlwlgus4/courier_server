# stage 1
# 도커 런타임에 사용될 도커 이미지를 지정한다
FROM openjdk:17-slim AS build
WORKDIR application
LABEL maintainer="<chlwlgus695@gmail.com>"

# JAR_FILE 변수를 정의
ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# JAR 파일을 이미지의 파일 시스템에 app.jar로 복사한다.
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17-slim
WORKDIR application

# jarmode 명령의 결과로 출력된 각 레이어를 복사한다.
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./

# 프로파일 설정 추가
ENV SPRING_PROFILES_ACTIVE=local

# org.springframework.boot.loader.JarLauncher로 애플리케이션을 실행한다.
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
