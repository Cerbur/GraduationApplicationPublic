mvn clean
mvn package -DskipTests
docker build -t cerbur.top/graduation-api ./wechat-api
docker build -t cerbur.top/mq ./wechat-mq
# docker run -d -p 8080:8080 cerbur.top/graduation-api -name graduation-api