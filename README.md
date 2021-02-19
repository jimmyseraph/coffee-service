# 如何使用
## 环境依赖
可以使用docker目录下的Dockerfile配置docker来搭建运行环境
+ mysql环境
```shell
docker build -t coffee_db:1.0 . 
docker run -p 3306:3306 --name coffee_mysql -d coffee_db:1.0
```
+ redis环境
```shell
docker build -t coffee_redis:1.0 .
docker run 6379:6379 --name coffee_redis -d coffee_redis:1.0
```
+ redis命令行连接
```shell
docker run  -it  --rm --link coffee_redis:redis coffee_redis:1.0 redis-cli -h coffee_redis
```
+ RabbitMQ环境
```shell
docker run -d --hostname coffee-rabbit --name coffee-rabbit -p 8080:15672 -p 5672:5672 rabbitmq:3-management
```
> 此为带有管理端的RabbitMQ，可以在`http://localhost:8080`查看，账号密码默认为guest/guest
+ ZipKin server环境
```shell script
docker run -d -p 9411:9411 openzipkin/zipkin
```
+ Docker registry环境
```shell script
docker run -d -p 5000:5000 --restart=always --name registry -v /opt/registry/:/var/lib/registry -v ~/docker_certs/:/certs -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.cert -e REGISTRY_HTTP_TLS_KEY=/certs/domain.key -e REGISTRY_STORAGE_DELETE_ENABLED=true registry:latest
```

## 部署服务
+ config服务
```shell script
docker run -d -p 10001:10001 --name config-service -v ~/.ssh:/root/.ssh docker.testops.vip:5000/beta-coffee-config:test
```
+ discovery服务
```shell script
docker run -d -p 10002:10002 --name discovery-service -e CONFIG_URL=${CONFIG_URL} -e PROFILE=test docker.testops.vip:5000/beta-coffee-discovery:test
```
+ gateway服务
```shell script
docker run -d -p 20000:20000 --name gateway-service -e CONFIG_URL=${CONFIG_URL} -e PROFILE=test -e EUREKA=${EUREKA} docker.testops.vip:5000/beta-coffee-gateway:test
```
+ account服务
```shell script
docker run -d -p 10003:10003 --name account-service -e CONFIG_URL=${CONFIG_URL} -e PROFILE=test -e EUREKA=${EUREKA} -e REDIS_H=${REDIS_HOST} -e MYSQL_H=${MYSQL_HOST} -e ZIPKIN=${ZIPKIN_URL} docker.testops.vip:5000/beta-coffee-account:test
```
+ order服务
```shell script
docker run -d -p 10004:10004 --name order-service -e CONFIG_URL=${CONFIG_URL} -e PROFILE=test -e EUREKA=${EUREKA} -e MYSQL_H=${MYSQL_HOST} -e MQ_H=${MQ_HOST} -e ZIPKIN=${ZIPKIN_URL} docker.testops.vip:5000/beta-coffee-order:test
```