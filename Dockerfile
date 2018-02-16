#FROM openjdk:8-jdk-alpine
#RUN  apk update && apk upgrade && apk add netcat-openbsd
#RUN mkdir -p /usr/local/authenticationservice
#ADD authentication-service-0.0.1-SNAPSHOT.jar /usr/local/authenticationservice/app.jar
#ADD run.sh run.sh
#RUN chmod +x run.sh
#CMD echo $PWD
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /usr/local/authenticationservice/app.jar" ]

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ONBUILD RUN ./gradlew clean build
ADD build/libs/microservice--user-service-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]