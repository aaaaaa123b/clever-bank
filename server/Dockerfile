FROM amazoncorretto:17 AS build

WORKDIR /app

COPY . .

RUN ./gradlew war

RUN find /app/build/libs -name '*.war' -exec mv {} /app/build/libs/ROOT.war \;

FROM tomcat:latest

COPY --from=build /app/build/libs/ROOT.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
