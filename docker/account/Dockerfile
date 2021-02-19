FROM openjdk:14.0.1

RUN mkdir /opt/microservice

COPY account.jar /opt/microservice/

WORKDIR /opt/microservice

ENV CONFIG_URL http://localhost:10001
ENV EUREKA localhost:10002
ENV PROFILE test
ENV REDIS_H localhost
ENV REDIS_P 6379
ENV MYSQL_H localhost
ENV MYSQL_P 3306
ENV ZIPKIN http://localhost:9411

CMD ["java", "-jar", "-DZIPKIN_URL=${ZIPKIN}", "-DMYSQL_HOST=${MYSQL_H}", "-DMYSQL_PORT=${MYSQL_P}", "-DREDIS_HOST=${REDIS_H}", "-DREDIS_PORT=${REDIS_P}", "-Dspring.cloud.config.uri=${CONFIG_URL}", "-Dspring.cloud.config.profile=${PROFILE}", "-DEUREKA_URL=${EUREKA}", "account.jar"]

EXPOSE 10003
