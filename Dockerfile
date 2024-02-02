FROM amazoncorretto:17-alpine-jdk

ARG PROFILE

ENV PROFILE_ENV=$PROFILE

MAINTAINER VinodKakarla

COPY target/iam-craft-demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=uat","-jar","/app.jar"]