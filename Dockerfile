FROM adoptopenjdk/openjdk11
LABEL maintainer="Totee <rnjstmdals6@google.com>"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]