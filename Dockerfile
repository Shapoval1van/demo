FROM openjdk:8-alpine
ADD target/demo-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Dspring.data.mongodb.host='mongodb' -Dfile.path='/image' -Djava.security.egd=file:/dev/./urandom -jar /app.jar
