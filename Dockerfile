FROM eclipse-temurin:23-jdk

# work dir
WORKDIR /app

# Copy jar from build context
COPY target/mainapp-0.0.1-SNAPSHOT.jar app.jar

# port no
EXPOSE 8092

# app run
ENTRYPOINT ["java", "-jar", "app.jar"]