FROM openjdk:10
ARG JAR_FILE=build/libs/security.api.fat.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","--add-modules","java.xml.bind","-jar","/app.jar"]