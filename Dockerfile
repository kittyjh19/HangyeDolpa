# 1. 빌드 스테이지: Maven을 사용해 .jar 파일 생성
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# 2. 프로젝트 소스 코드 복사 후 빌드
COPY . .
RUN mvn clean package -DskipTests

# 3. 실행 스테이지: 빌드된 .jar 파일을 실행
FROM openjdk:21-jdk-slim
WORKDIR /app

# 4. 빌드 스테이지에서 생성된 .jar 파일을 가져옴
COPY --from=builder /app/target/*.jar app.jar

# 5. 실행 명령어
CMD ["java", "-jar", "app.jar"]
