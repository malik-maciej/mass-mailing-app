FROM openjdk:8u191-jre-alpine3.9
ADD target/mass-mailing-app-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar mass-mailing-app-0.0.1-SNAPSHOT.jar