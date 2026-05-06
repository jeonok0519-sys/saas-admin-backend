FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

# 关键修复：直接读取 target 里的真实 jar 名
ENTRYPOINT ["java", "-jar", "/app/target/saas-admin-1.0.0.jar"]