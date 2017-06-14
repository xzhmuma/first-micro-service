FROM docker.io/java:8u111
VOLUME /tmp
ADD eureka_server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]