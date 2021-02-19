FROM openjdk:14.0.1

RUN mkdir /opt/microservice

COPY config.jar /opt/microservice/

WORKDIR /opt/microservice

CMD ["java", "-jar", "config.jar"]

EXPOSE 10001
