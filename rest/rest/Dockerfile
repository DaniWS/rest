FROM openjdk:8-jdk
MAINTAINER Daniel Vilela (d.vilela@alumnos.upm.es)
RUN apt-get update
RUN apt-get install -y maven
RUN mvn -version
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
EXPOSE 8080
#RUN ln -sf /log/logfile.log
RUN mvn package
CMD mvn exec:java

