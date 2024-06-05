FROM openjdk:17-oracle

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} emailservice.jar

ENTRYPOINT ["java", "-jar", "/emailservice.jar"]

EXPOSE 8093