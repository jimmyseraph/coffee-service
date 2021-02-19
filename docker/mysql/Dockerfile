FROM mysql:8.0

ENV MYSQL_DATABASE coffeedb
ENV MYSQL_ROOT_PASSWORD Aa-123456
ENV MYSQL_USER coffee
ENV MYSQL_PASSWORD Coffee123!
COPY ./sql/*.sql /docker-entrypoint-initdb.d/

