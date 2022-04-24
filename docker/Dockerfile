FROM openjdk:11-jre-slim

RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/logcollection-*.jar /log-collector-server.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/log-collector-server.jar"]