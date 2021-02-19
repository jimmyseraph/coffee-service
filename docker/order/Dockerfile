FROM openjdk:14.0.1

RUN mkdir /opt/microservice

COPY order.jar /opt/microservice/

WORKDIR /opt/microservice

ENV CONFIG_URL http://localhost:10001
ENV EUREKA localhost:10002
ENV PROFILE test
ENV MYSQL_H localhost
ENV MYSQL_P 3306
ENV MQ_H localhost
ENV MQ_P 5672
ENV ZIPKIN http://localhost:9411

CMD ["java", "-jar", "-DZIPKIN_URL=${ZIPKIN}", "-DMYSQL_HOST=${MYSQL_H}", "-DMYSQL_PORT=${MYSQL_P}", "-DMQ_HOST=${MQ_H}", "-DMQ_PORT=${MQ_P}", "-Dspring.cloud.config.uri=${CONFIG_URL}", "-Dspring.cloud.config.profile=${PROFILE}", "-DEUREKA_URL=${EUREKA}", "order.jar"]

EXPOSE 10004
