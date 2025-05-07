FROM openjdk:21
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "target/notifier-0.0.1-SNAPSHOT.jar"]
