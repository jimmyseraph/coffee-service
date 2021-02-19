FROM openjdk:14.0.1

RUN mkdir /opt/microservice

COPY discovery.jar /opt/microservice/

WORKDIR /opt/microservice

ENV CONFIG_URL http://localhost:10001
ENV PROFILE test

CMD ["java", "-jar", "-Dspring.cloud.config.uri=${CONFIG_URL}", "-Dspring.cloud.config.profile=${PROFILE}", "discovery.jar"]

EXPOSE 10002
