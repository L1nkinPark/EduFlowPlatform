FROM openjdk:17-slim
#FROM openjdk:21-slim

VOLUME /tmp

COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java","-jar","/app.jar"]
