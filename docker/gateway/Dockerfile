FROM openjdk:14.0.1

RUN mkdir /opt/microservice

COPY gateway.jar /opt/microservice/

WORKDIR /opt/microservice

ENV CONFIG_URL http://localhost:10001
ENV EUREKA localhost:10002
ENV PROFILE test

CMD ["java", "-jar", "-Dspring.cloud.config.uri=${CONFIG_URL}", "-Dspring.cloud.config.profile=${PROFILE}", "-DEUREKA_URL=${EUREKA}", "gateway.jar"]

EXPOSE 20000
