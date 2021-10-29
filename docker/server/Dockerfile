FROM openjdk:11

RUN mkdir /server

COPY api-server.jar /server

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5000", "-jar", "/server/api-server.jar", "--spring.profiles.active=dev"]
