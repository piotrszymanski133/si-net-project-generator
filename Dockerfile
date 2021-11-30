FROM maven:3.8.3-openjdk-17 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean compile assembly:single
RUN yum install -y procps

FROM openjdk:15
COPY --from=build /usr/src/app/target/generator-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/generator-1.0-SNAPSHOT.jar
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/app/generator-1.0-SNAPSHOT.jar"]
